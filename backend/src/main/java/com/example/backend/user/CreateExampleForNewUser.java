package com.example.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.backend.card.CardEntity;
import com.example.backend.card.CardRepository;
import com.example.backend.deck.DeckEntity;
import com.example.backend.deck.DeckRepository;
import com.example.backend.security.user.UserEntity;
import com.example.backend.trida.TridaEntity;
import com.example.backend.trida.TridaRepository;

@Component
public class CreateExampleForNewUser {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private TridaRepository tridaRepository;

    /**
     * @param userEntity
     * 
     *                   Creates 10 words for english animals as an example for this
     *                   user
     */
    @Async
    public void CreateExample(UserEntity userEntity) {
        System.out.println("Create example for new user");

        TridaEntity anglictinaTrida = new TridaEntity();
        anglictinaTrida.setUserEntity(userEntity);
        anglictinaTrida.setName("English");
        tridaRepository.save(anglictinaTrida);

        DeckEntity zvirataDeck = new DeckEntity();
        zvirataDeck.setTridaEntity(anglictinaTrida);
        zvirataDeck.setName("Animals");
        deckRepository.save(zvirataDeck);


        //Chatgpt wrote the following.
        String[] englishToCzechAnimals = {
            "Cat", "Kočka",
            "Dog", "Pes",
            "Horse", "Kůň",
            "Bird", "Pták",
            "Fish", "Ryba",
            "Rabbit", "Králík",
            "Elephant", "Slon",
            "Turtle", "Želva",
            "Lion", "Lev",
            "Butterfly", "Motýl",
            "Monkey", "Opice",
            "Dolphin", "Delfín",
            "Giraffe", "Žirafa",
            "Penguin", "Tučňák",
            "Kangaroo", "Klokan",
            "Snake", "Had",
            "Tiger", "Tygr",
            "Panda", "Panda",
            "Gorilla", "Gorila",
            "Zebra", "Zebra",
            "Hippopotamus", "Hroch",
            "Ostrich", "Pštros",
            "Cheetah", "Gepard",
            "Koala", "Koala",
            "Squirrel", "Veverka",
            "Polar Bear", "Lední Medvěd",
            "Duck", "Kachna"
        };


        for (int i = 0; i < englishToCzechAnimals.length; i += 2) {
            String englishWord = englishToCzechAnimals[i];
            String czechWord = englishToCzechAnimals[i + 1];

            CardEntity cardEntity = new CardEntity();
            cardEntity.setDeckEntity(zvirataDeck);
            cardEntity.setVisiblePart(englishWord); 
            cardEntity.setHiddenPart(czechWord);  

            cardRepository.save(cardEntity);
        }

    }
}
