package com.example.backend.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.card.CardRepository;
import com.example.backend.deck.DeckRepository;
import com.example.backend.security.Helper;
import com.example.backend.trida.TridaRepository;

@RequestMapping("/export")
@RestController
public class ExportController {

    @Autowired
    private TridaRepository tridaRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CardRepository cardRepository;


    @GetMapping("/allcardshumanformat")
    public @ResponseBody ResponseEntity<String> allCardsHumanFormat(){

        var tridas = tridaRepository.findAllByUserEntity(Helper.getUserEntity());

        String data = "";


        for (var trida : tridas){
            data += "\n" + "Class: "+trida.getName();
            var decks = deckRepository.findByTridaEntity(trida);
            for (var deck : decks){
                data += "\n" + "Deck: "+deck.getName();
                var cards = cardRepository.findByDeckEntity(deck);
                for (var card : cards){
                    data += "\n" + ""+card.getVisiblePart() + " : "+ card.getHiddenPart();
                }
            }
        }

        return new ResponseEntity<>(data,HttpStatus.OK);
    }
}
