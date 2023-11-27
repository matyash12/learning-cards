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
    public @ResponseBody ResponseEntity<DeckEntity> get(@PathVariable long id) {
        try {
            var deck = deckRepository.findById(id).get();
            if (Helper.hasRightsForDeck(deck)) {
                return new ResponseEntity<>(deck, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/new")
    public @ResponseBody ResponseEntity<DeckEntity> create(@RequestParam String name, @RequestParam long tridaid) {
        TridaEntity trida;
        try {
            trida = tridaRepository.findById(tridaid).get();
            if (!Helper.hasRightsForTrida(trida)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        DeckEntity deck = new DeckEntity();
        deck.setName(name);
        deck.setTridaEntity(trida);
        deckRepository.save(deck);

        return new ResponseEntity<>(deck, HttpStatus.OK);
    }

    @PostMapping("/find")
    public @ResponseBody ResponseEntity<List<DeckEntity>> getCards(@RequestParam long tridaid) {
        TridaEntity trida;
        try {
            trida = tridaRepository.findById(tridaid).get();
            if (!Helper.hasRightsForTrida(trida)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var decks = deckRepository.findByTridaEntity(trida);

        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<String> delete(@RequestParam long id) {
        try {
            var deck = deckRepository.findById(id).get();
            if (!Helper.hasRightsForDeck(deck)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        deckRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public @ResponseBody ResponseEntity<Iterable<DeckEntity>> getAll() {
        try {
            var decks = deckRepository.findAll();
            List<DeckEntity> allowedDecks = new ArrayList<>();

            for (var deck : decks) {
                if (Helper.hasRightsForDeck(deck)) {
                    decks.add(deck);
                }
            }
            return new ResponseEntity<>(allowedDecks, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/update")
    public @ResponseBody ResponseEntity<String> update(@RequestParam long id,
            @RequestParam(required = false) String name) {
        try {
            var deck = deckRepository.findById(id).get();

            if (!Helper.hasRightsForDeck(deck)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            deck.setName(name);
            deckRepository.save(deck);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
