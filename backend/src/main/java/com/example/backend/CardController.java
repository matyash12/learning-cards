package com.example.backend;

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

@RestController
@RequestMapping("/card")
@CrossOrigin(origins = "*")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<CardEntity> getCard(@PathVariable long id) {
        var c = cardRepository.findById(id);

        if (c.isPresent()) {
            return new ResponseEntity<>(c.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<Iterable<CardEntity>> getAllCards() {
        // This returns a JSON or XML with the users
        return new ResponseEntity<>(cardRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/new")
    public @ResponseBody ResponseEntity<String> createCard(@RequestParam String hiddenPart,
            @RequestParam String visiblePart) {

        CardEntity card = new CardEntity();
        card.setHiddenPart(hiddenPart);
        card.setVisiblePart(visiblePart);

        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<String> deleteCard(@RequestParam long id) {
        cardRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public @ResponseBody ResponseEntity<String> updateCard(@RequestParam long id, @RequestParam String hiddenPart,
            @RequestParam String visiblePart) {
        this.deleteCard(id);
        this.createCard(hiddenPart, visiblePart);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
