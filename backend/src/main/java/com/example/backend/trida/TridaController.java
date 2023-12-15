package com.example.backend.trida;

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

import com.example.backend.ApiMessages;
import com.example.backend.ApiResponse;
import com.example.backend.security.Helper;

@RestController
@RequestMapping("/tridy")
@CrossOrigin(origins = "*")
public class TridaController {
    @Autowired
    private TridaRepository tridaRepository;

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ApiResponse> get(@PathVariable long id) {
        try {
            var trida = tridaRepository.findById(id).get();
            if (Helper.hasRightsForTrida(trida)) {
                return new ApiResponse(trida, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
            } else {
                return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
            }

        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }
    }

    @PostMapping("/new")
    public @ResponseBody ResponseEntity<ApiResponse> create(@RequestParam String name) {
        var trida = new TridaEntity();
        trida.setName(name);

        trida.setUserEntity(Helper.getUserEntity());

        tridaRepository.save(trida);
        return new ApiResponse(null, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<ApiResponse> delete(@RequestParam long id) {
        try {
            var tridaToDelete = tridaRepository.findById(id).get();

            if (Helper.hasRightsForTrida(tridaToDelete)) {
                tridaRepository.deleteById(id);
                return new ApiResponse(null, ApiMessages.NO_CONTENT, HttpStatus.NO_CONTENT).toResponseEntity();
            } else {
                return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }
    }

    @GetMapping("/all")
    public @ResponseBody ResponseEntity<ApiResponse> getAll() {
        return new ApiResponse(tridaRepository.findAllByUserEntity(Helper.getUserEntity()), ApiMessages.OK, HttpStatus.OK).toResponseEntity();
    }

    @PostMapping("/update")
    public @ResponseBody ResponseEntity<ApiResponse> update(@RequestParam long id,
            @RequestParam(required = false) String name) {
        try {
            var trida = tridaRepository.findById(id).get();

            if (Helper.hasRightsForTrida(trida)) {
                trida.setName(name);
                tridaRepository.save(trida);
                return new ApiResponse(null, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
            } else {
                return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }

    }

    

}
