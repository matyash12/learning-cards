package com.example.backend.card;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.ApiMessages;
import com.example.backend.ApiResponse;
import com.example.backend.AppVariables;
import com.example.backend.deck.DeckRepository;
import com.example.backend.images.ImageController;
import com.example.backend.images.ImageRepository;
import com.example.backend.security.Helper;

@RestController
@RequestMapping("/card")
@CrossOrigin(origins = "*")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private ImageController imageController;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ApiResponse> getCard(@PathVariable long id) {

        try {
            var card = cardRepository.findById(id).get();
            if (!Helper.hasRightsForCard(card)) {
                return new ApiResponse(null,ApiMessages.FORBIDDEN,HttpStatus.FORBIDDEN).toResponseEntity();
            }
            return new ApiResponse(card,ApiMessages.OK,HttpStatus.OK).toResponseEntity();
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.NOT_FOUND, HttpStatus.NOT_FOUND).toResponseEntity();
        }

    }

    @PostMapping("/find")
    public @ResponseBody ResponseEntity<ApiResponse> getCards(@RequestParam long deckid) {
        try {
            var deck = deckRepository.findById(deckid).get();
            if (!Helper.hasRightsForDeck(deck)) {
                return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
            }
            var cards = cardRepository.findByDeckEntity(deck);
            return new ApiResponse(cards, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }
    }

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<ApiResponse> getAllCards() {
        var allCards = cardRepository.findAll();
        var allowedCards = new ArrayList<CardEntity>();

        for (var card : allCards) {
            if (Helper.hasRightsForCard(card)) {
                allowedCards.add(card);
            }

        }
        return new ApiResponse(allowedCards, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
    }

    @PostMapping("/new")
    public @ResponseBody ResponseEntity<ApiResponse> createCard(@RequestParam String hiddenPart,
            @RequestParam String visiblePart, @RequestParam(defaultValue = "0") int mark,
            @RequestParam long deckid,
            @RequestParam(value = "hiddenPartImageFile", required = false) MultipartFile hiddenPartImageFile,
            @RequestParam(value = "visiblePartImageFile", required = false) MultipartFile visiblePartImageFile) {

        var deck = deckRepository.findById(deckid).get();
        if (!Helper.hasRightsForDeck(deck)) {
            return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
        }
        CardEntity card = new CardEntity(hiddenPart, visiblePart, mark, deck);
        cardRepository.save(card);
        if (hiddenPartImageFile != null) {
            var hiddenPartImage = imageController.upload(hiddenPartImageFile, card, 0);
            if (hiddenPartImage == null) {
                return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
            }
        }
        if (visiblePartImageFile != null) {
            var visiblePartImage = imageController.upload(visiblePartImageFile, card, 1);
            if (visiblePartImage == null) {
                return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
            }

        }
        return new ApiResponse(null, ApiMessages.CREATED, HttpStatus.CREATED).toResponseEntity();
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<ApiResponse> deleteCard(@RequestParam long id) {
        var card = cardRepository.findById(id).get();
        if (!Helper.hasRightsForCard(card)) {
            return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
        }

        cardRepository.deleteById(id);
        return new ApiResponse(null, ApiMessages.NO_CONTENT, HttpStatus.NO_CONTENT).toResponseEntity();
    }

    @PostMapping("/update")
    public @ResponseBody ResponseEntity<ApiResponse> updateCard(@RequestParam long id,
            @RequestParam(required = false) String hiddenPart,
            @RequestParam(required = false) String visiblePart,
            @RequestParam(required = false, defaultValue = "-1") int mark,
            @RequestParam(required = false, defaultValue = "-1") long deckid,
            @RequestParam(value = "hiddenPartImageFile", required = false) MultipartFile hiddenPartImageFile,
            @RequestParam(value = "hiddenPartImageFileDelete", required = false, defaultValue = "false") Boolean hiddenPartImageFileDelete,
            @RequestParam(value = "visiblePartImageFile", required = false) MultipartFile visiblePartImageFile,
            @RequestParam(value = "visiblePartImageFileDelete", required = false, defaultValue = "false") Boolean visiblePartImageFileDelete

    ) {

        var card = cardRepository.findById(id).get();
        if (!Helper.hasRightsForCard(card)) {
            return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
        }

        if (hiddenPart != null) {
            card.setHiddenPart(hiddenPart);
        }
        if (visiblePart != null) {
            card.setVisiblePart(visiblePart);
        }
        if (mark != -1) {
            card.setMark(mark);
        }
        if (deckid != -1) {
            card.setDeckEntity(deckRepository.findById(id).get());
        }
        if (hiddenPartImageFile != null) {
            var hiddenPartImage = imageController.upload(hiddenPartImageFile, card, 0);
            if (hiddenPartImage == null) {
                return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
            }

        }
        if (visiblePartImageFile != null) {
            var visiblePartImage = imageController.upload(visiblePartImageFile, card, 1);
            if (visiblePartImage == null) {
                return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
            }

        }
        if (hiddenPartImageFileDelete == true) {
            var images = imageRepository.findByCardEntity(card);
            for (int i = 0; i < images.size(); i++) {
                var image = images.get(i);
                if (image.getPosition() == AppVariables.IMAGE_HIDDEN_PART_POSITION) {
                    var imageId = image.getId();
                    imageRepository.deleteById(imageId);
                }

            }

        }
        if (visiblePartImageFileDelete == true) {
            var images = imageRepository.findByCardEntity(card);
            for (int i = 0; i < images.size(); i++) {
                var image = images.get(i);
                if (image.getPosition() == AppVariables.IMAGE_VISIBLE_PART_POSITION) {
                    var imageId = image.getId();
                    imageRepository.deleteById(imageId);
                }
            }

        }

        cardRepository.save(card);
        return new ApiResponse(null, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
    }

}
