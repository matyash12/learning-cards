package com.example.backend.learn.LearnSession;

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
import com.example.backend.deck.DeckRepository;
import com.example.backend.security.Helper;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/learnsession")
@CrossOrigin(origins = "*")
public class LearnSessionController {

    @Autowired
    private LearnSessionRepository learnSessionRepository;

    @Autowired
    private DeckRepository deckRepository;

    @PostMapping(path = "/new")
    public @ResponseBody ResponseEntity<ApiResponse> createSession(@RequestParam Long deckId,
            @RequestParam int numberOfCardsInActiveLearning) {

        // check if user has rights for this
        var deck = deckRepository.findById(deckId).get();
        if (!Helper.hasRightsForDeck(deck)) {
            return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
        }

        // check variable numberOfCardsInActiveLearning
        if (numberOfCardsInActiveLearning < 1) {
            return new ApiResponse(null, ApiMessages.NO_CONTENT, HttpStatus.NO_CONTENT).toResponseEntity();
        }

        // create the session
        LearnSessionEntity learnSessionEntity = new LearnSessionEntity(deck, numberOfCardsInActiveLearning);
        learnSessionRepository.save(learnSessionEntity);

        return new ApiResponse(learnSessionEntity, ApiMessages.CREATED, HttpStatus.CREATED).toResponseEntity();
    }

    @PostMapping(path = "/update")
    public @ResponseBody ResponseEntity<ApiResponse> updateSession(@RequestParam Long sessionId,
            @RequestParam(defaultValue = "0") int numberOfCardsInActiveLearning) {

        // get session
        var sessionOptional = learnSessionRepository.findById(sessionId);
        if (sessionOptional.isEmpty()) {
            return new ApiResponse(null, ApiMessages.NOT_FOUND, HttpStatus.NOT_FOUND).toResponseEntity();
        }
        var session = sessionOptional.get();

        // check rights
        if (!Helper.hasRightsForLearnSession(session)) {
            return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
        }

        // update numberOfCardsInActiveLearning
        if (numberOfCardsInActiveLearning != 0) {
            if (numberOfCardsInActiveLearning < 1) {
                return new ApiResponse(null, ApiMessages.NO_CONTENT, HttpStatus.NO_CONTENT).toResponseEntity();
            }
            session.setNumberOfCardsInActiveLearning(numberOfCardsInActiveLearning);
        }

        //save the changes
        learnSessionRepository.save(session);

        //return
        return new ApiResponse(null, ApiMessages.OK, HttpStatus.OK).toResponseEntity(); 

    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ApiResponse> getSession(@PathVariable long id){

        // get session
        var sessionOptional = learnSessionRepository.findById(id);
        if (sessionOptional.isEmpty()) {
            return new ApiResponse(null, ApiMessages.NOT_FOUND, HttpStatus.NOT_FOUND).toResponseEntity();
        }
        var session = sessionOptional.get();

        // check rights
        if (!Helper.hasRightsForLearnSession(session)) {
            return new ApiResponse(null, ApiMessages.FORBIDDEN, HttpStatus.FORBIDDEN).toResponseEntity();
        }

        //add missing data
        session.setProgress(learnSessionRepository.findProgressByLearnSessionId(id));
        
        //return
        return new ApiResponse(session, ApiMessages.OK, HttpStatus.OK).toResponseEntity(); 
    }
}
