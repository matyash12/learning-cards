
package com.example.backend.learn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.ApiMessages;
import com.example.backend.ApiResponse;
import com.example.backend.card.CardEntity;
import com.example.backend.card.CardRepository;
import com.example.backend.learn.LearnChange.LearnChangeEntity;
import com.example.backend.learn.LearnChange.LearnChangeRepository;
import com.example.backend.learn.LearnSession.LearnSessionEntity;
import com.example.backend.learn.LearnSession.LearnSessionRepository;
import com.example.backend.learn.SupportClasses.CardWithMarkCount;
import com.example.backend.learn.SupportClasses.MarkCount;

@RestController
@RequestMapping("/learn")
@CrossOrigin(origins = "*")
public class LearnController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private LearnSessionRepository learnSessionRepository;

    @Autowired
    private LearnChangeRepository learnChangeRepository;

    @PostMapping(path = "/next")
    public @ResponseBody ResponseEntity<ApiResponse> getNextCard(@RequestParam Long id) {
        // check if id exists
        Optional<LearnSessionEntity> sessionOptional = learnSessionRepository.findById(id);
        if (sessionOptional.isEmpty()) {
            return new ApiResponse(null, ApiMessages.NOT_FOUND, HttpStatus.NOT_FOUND).toResponseEntity();
        }
        LearnSessionEntity session = sessionOptional.get();

        // get records of this session and remove duplicates
        List<LearnChangeEntity> learnChanges = new ArrayList<>();
        var learnChangesOptional = learnChangeRepository.findByLearnSessionEntityOrderByIdAsc(session);
        for (var learnChange : learnChangesOptional){
            var canAdd = true;
            for (var checkLearnChange : learnChanges){
                if (learnChange.getCardEntity().getId() == checkLearnChange.getCardEntity().getId()){
                    canAdd = false;
                }
            }
            if (canAdd){
                learnChanges.add(learnChange);
            }
        }
        //System.out.println("get records of this session and remove duplicates");
        //System.out.println(learnChanges);

        // get cards to re-learn from the session
        List<CardEntity> cardsToReLearn = new ArrayList<>();
        for (var learnChange : learnChanges) {
            CardEntity card = learnChange.getCardEntity();
            if (card.getMark() > 1) {
                cardsToReLearn.add(card);
            }
        }
        //System.out.println("get cards to re-learn from the session");
        //System.out.println(cardsToReLearn);

        // if there isnt enough cards in cardsToReLearn add them from cards with mark =
        // 0
        var cardsInDeck = cardRepository.findByDeckEntity(session.getDeckEntity());
        while (cardsToReLearn.size() < session.getNumberOfCardsInActiveLearning()) {
            var cardAdded = false;
            for (var card : cardsInDeck) {
                if (card.getMark() != 1) {
                    var cardIdOkToBeAdded = true;
                    for (var checkedCard : cardsToReLearn) {
                        if (checkedCard.getId() == card.getId()) {
                            cardIdOkToBeAdded = false;
                        }
                    }
                    if (cardIdOkToBeAdded) {
                        cardsToReLearn.add(card);
                        cardAdded = true;
                    }
                }
            }
            if (cardAdded == false) {
                break;
            }
        }
        //System.out.println(" if there isnt enough cards in cardsToReLearn add them from cards with mark = 0");
        //System.out.println(cardsToReLearn);


        //if the cardsToReLearn is still too small some with mark = 1
        List<CardEntity> possibleCardsToAdd = new ArrayList<>();
        for (var card : cardsInDeck){
            var safeToAdd = true;
            for (var checkCard : cardsToReLearn){
                if (card.getId() == checkCard.getId()){
                    safeToAdd = false;
                }
            }
            if (safeToAdd){
                possibleCardsToAdd.add(card);
            }
        }
        Random random = new Random();
        while (cardsToReLearn.size() < session.getNumberOfCardsInActiveLearning()) {
            if (possibleCardsToAdd.size() > 0){
                int randomIndex = random.nextInt(possibleCardsToAdd.size());
                var randomCard = possibleCardsToAdd.get(randomIndex);
                possibleCardsToAdd.remove(randomCard);
                cardsToReLearn.add(randomCard);
            }else{
                break;
            }
        }
        //System.out.println("if the cardsToReLearn is still too small some with mark = 1");
        //System.out.println(cardsToReLearn);

        //set the id
        if (cardsToReLearn.size() > session.getCurrentIndex()){
            session.setCurrentIndex(session.getCurrentIndex()+1);
        }
        if (cardsToReLearn.size() == session.getCurrentIndex()){
            session.setCurrentIndex(0);
        }
        if (cardsToReLearn.size() < session.getCurrentIndex()){
            session.setCurrentIndex(0);
        }

        //get the card to return
        var theCard = cardsToReLearn.get(session.getCurrentIndex());

        //add card to LearnChangeRepository
        var learnChange = new LearnChangeEntity(session, theCard);
        learnChangeRepository.save(learnChange);

        

        //count of each mark
        Double[] markCountArray = cardRepository.TableMarksByDeckEntityId(session.getDeckEntity().getId());
        MarkCount markCount = new MarkCount(markCountArray[0].intValue(), markCountArray[1].intValue(), markCountArray[2].intValue(), markCountArray[3].intValue(), markCountArray[4].intValue(), markCountArray[5].intValue());
        //return theCard

        CardWithMarkCount cardWithMarkCount = new CardWithMarkCount(theCard, markCount);
        
        return new ApiResponse(cardWithMarkCount, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
    }



}


