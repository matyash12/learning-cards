package com.example.backend.deck;


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
        var t = deckRepository.findById(id);
        if (t.isPresent()) {
            return new ResponseEntity<>(t.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public @ResponseBody ResponseEntity<DeckEntity> create(@RequestParam String name, @RequestParam long tridaid) {
        DeckEntity deck = new DeckEntity();
        deck.setName(name);
        deck.setTridaEntity(tridaRepository.findById(tridaid).get());

        deckRepository.save(deck);
        return new ResponseEntity<>(deck,HttpStatus.OK);
    }

     @PostMapping("/find")
    public @ResponseBody ResponseEntity<List<DeckEntity>> getCards(@RequestParam long tridaid) {
        var decks = deckRepository.findByTridaEntity(tridaRepository.findById(tridaid).get());

        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<String> delete(@RequestParam long id) {
        deckRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public @ResponseBody ResponseEntity<Iterable<DeckEntity>> getAll() {
        return new ResponseEntity<>(deckRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/update")
    public @ResponseBody ResponseEntity<String> update(@RequestParam long id,
            @RequestParam(required = false) String name) {
        var deck = deckRepository.findById(id).get();
        deck.setName(name);
        deckRepository.save(deck);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}