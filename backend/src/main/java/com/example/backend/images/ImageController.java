package com.example.backend.images;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.backend.ApiMessages;
import com.example.backend.ApiResponse;
import com.example.backend.card.CardEntity;
import com.example.backend.card.CardRepository;
import com.example.backend.deck.DeckEntity;
import com.example.backend.minio.MinioService;
import com.example.backend.security.Helper;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CardRepository cardRepository;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucketname.image}")
    private String imageBucketName;

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ApiResponse> get(@PathVariable long id) {
        try {
            var img = imageRepository.findById(id).get();
            if (Helper.hasRightsForImage(img)) {
                return new ApiResponse(img, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }
        return new ApiResponse(null, ApiMessages.NOT_FOUND, HttpStatus.NOT_FOUND).toResponseEntity();

    }

    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] show(@PathVariable long id) {

        var _img = imageRepository.findById(id);
        if (_img.isEmpty()) {
            return null;
        }
        var img = _img.get();
        if (!Helper.hasRightsForImage(img)) {
            return null;
        }

        var minioClient = getMinioClient();

        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(imageBucketName)
                            .object(img.getId() + "")
                            .build());
            return IOUtils.toByteArray(stream);

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;

    }

    @PostMapping("/find")
    public @ResponseBody ResponseEntity<ApiResponse> getCards(@RequestParam long cardid) {
        try {
            var card = cardRepository.findById(cardid).get();
            if (!Helper.hasRightsForCard(card)) {
                return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
            }
            var images = imageRepository.findByCardEntity(card);
            return new ApiResponse(images, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }
    }

    public ImageEntity upload(MultipartFile file, CardEntity cardEntity, int position) {
        // TODO make sure its "clean" - getOriginalFilename()
        ImageEntity imageEntity = new ImageEntity(file.getOriginalFilename(), cardEntity, position); //TODO replace image with actual filename 
        imageRepository.save(imageEntity);

        var minioClient = getMinioClient();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(imageBucketName).object(imageEntity.getId() + "").stream(
                            file.getInputStream(), file.getSize(), -1)
                            .contentType("image/jpeg")//TODO other than jpeg?
                            .build());
        } catch (Exception e) {
            System.out.println(e);
            
            return null;
        }

        return imageEntity;
    }

    @GetMapping("/all")
    public @ResponseBody ResponseEntity<ApiResponse> getAll() {
        try {
            var images = imageRepository.findAll();
            List<ImageEntity> allowedImages = new ArrayList<>();

            for (var image : images) {
                if (Helper.hasRightsForImage(image)) {
                    allowedImages.add(image);
                }
            }
            return new ApiResponse(allowedImages, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }
    }

    private MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
