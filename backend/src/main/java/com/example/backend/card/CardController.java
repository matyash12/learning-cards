package com.example.backend.card;

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

import com.example.backend.deck.DeckRepository;
import com.example.backend.security.Helper;

@RestController
@RequestMapping("/card")
@CrossOrigin(origins = "*")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<CardEntity> getCard(@PathVariable long id) {

        try {
            var card = cardRepository.findById(id).get();
            if (!Helper.hasRightsForCard(card)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(card, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/find")
    public @ResponseBody ResponseEntity<List<CardEntity>> getCards(@RequestParam long deckid) {
        try {
            var deck = deckRepository.findById(deckid).get();
            if (!Helper.hasRightsForDeck(deck)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            var cards = cardRepository.findByDeckEntity(deck);
            return new ResponseEntity<>(cards, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<Iterable<CardEntity>> getAllCards() {
        var allCards = cardRepository.findAll();
        List<CardEntity> allowedCards = new ArrayList<>();

        for (var card : allCards) {
            if (Helper.hasRightsForCard(card)) {
                allowedCards.add(card);
            }

        }

        return new ResponseEntity<>(allowedCards, HttpStatus.OK);
    }

    @PostMapping("/new")
    public @ResponseBody ResponseEntity<String> createCard(@RequestParam String hiddenPart,
            @RequestParam String visiblePart, @RequestParam(defaultValue = "0") int mark,
            @RequestParam long deckid) {
        var deck = deckRepository.findById(deckid).get();
        if (!Helper.hasRightsForDeck(deck)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CardEntity card = new CardEntity();
        card.setHiddenPart(hiddenPart);
        card.setVisiblePart(visiblePart);
        card.setMark(mark);
        card.setDeckEntity(deck);

        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<String> deleteCard(@RequestParam long id) {
        var card = cardRepository.findById(id).get();
        if (!Helper.hasRightsForCard(card)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        cardRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public @ResponseBody ResponseEntity<String> updateCard(@RequestParam long id,
            @RequestParam(required = false) String hiddenPart,
            @RequestParam(required = false) String visiblePart,
            @RequestParam(required = false, defaultValue = "-1") int mark,
            @RequestParam(required = false, defaultValue = "-1") long deckid) {

        var card = cardRepository.findById(id).get();
        if (!Helper.hasRightsForCard(card)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
        cardRepository.save(card);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
