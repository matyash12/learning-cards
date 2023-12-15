package com.example.backend.deck;

import java.util.ArrayList;
import java.util.List;

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
import com.example.backend.trida.TridaEntity;
import com.example.backend.trida.TridaRepository;

@RestController
@RequestMapping("/deck")
@CrossOrigin(origins = "*")
public class DeckController {
    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private TridaRepository tridaRepository;

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ApiResponse> get(@PathVariable long id) {
        try {
            var deck = deckRepository.findById(id).get();
            if (Helper.hasRightsForDeck(deck)) {
                return new ApiResponse(deck, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
            } else {
                return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
            }

        } catch (Exception e) {
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }

    }

    @PostMapping("/new")
    public @ResponseBody ResponseEntity<ApiResponse> create(@RequestParam String name, @RequestParam long tridaid) {
        TridaEntity trida;
        try {
            trida = tridaRepository.findById(tridaid).get();
            if (!Helper.hasRightsForTrida(trida)) {
                return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();

            }
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }

        DeckEntity deck = new DeckEntity();
        deck.setName(name);
        deck.setTridaEntity(trida);
        deckRepository.save(deck);
        return new ApiResponse(deck, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
    }

    @PostMapping("/find")
    public @ResponseBody ResponseEntity<ApiResponse> getCards(@RequestParam long tridaid) {
        TridaEntity trida;
        try {
            trida = tridaRepository.findById(tridaid).get();
            if (!Helper.hasRightsForTrida(trida)) {
                return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }

        var decks = deckRepository.findByTridaEntity(trida);
        return new ApiResponse(decks, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<ApiResponse> delete(@RequestParam long id) {
        try {
            var deck = deckRepository.findById(id).get();
            if (!Helper.hasRightsForDeck(deck)) {
                return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }

        deckRepository.deleteById(id);
        return new ApiResponse(null, ApiMessages.NO_CONTENT, HttpStatus.NO_CONTENT).toResponseEntity();
    }

    @GetMapping("/all")
    public @ResponseBody ResponseEntity<ApiResponse> getAll() {
        try {
            var decks = deckRepository.findAll();
            List<DeckEntity> allowedDecks = new ArrayList<>();

            for (var deck : decks) {
                if (Helper.hasRightsForDeck(deck)) {
                    decks.add(deck);
                }
            }
            return new ApiResponse(allowedDecks, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }
    }

    @PostMapping("/update")
    public @ResponseBody ResponseEntity<ApiResponse> update(@RequestParam long id,
            @RequestParam(required = false) String name) {
        try {
            var deck = deckRepository.findById(id).get();

            if (!Helper.hasRightsForDeck(deck)) {
                return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
            }
            deck.setName(name);
            deckRepository.save(deck);
            return new ApiResponse(null, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
        }

    }

}
