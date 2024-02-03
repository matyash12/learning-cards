package com.example.backend.learn.LearnSession;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.backend.deck.DeckEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "learnsession")
public class LearnSessionEntity {

    // constructors
    public LearnSessionEntity() {
    }

    public LearnSessionEntity(DeckEntity deckEntity, int numberOfCardsInActiveLearning) {
        this.deckEntity = deckEntity;
        this.currentIndex = 0;
        this.numberOfCardsInActiveLearning = numberOfCardsInActiveLearning;
    }

    // variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DeckEntity deckEntity;

    // the id for "imaginary" array in LearnControler
    // eg. for card on which index to select
    private int currentIndex;

    // number of cards which are change frequently
    private int numberOfCardsInActiveLearning;

    // functions
    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getNumberOfCardsInActiveLearning() {
        return numberOfCardsInActiveLearning;
    }

    public void setNumberOfCardsInActiveLearning(int numberOfCardsInActiveLearning) {
        this.numberOfCardsInActiveLearning = numberOfCardsInActiveLearning;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeckEntity getDeckEntity() {
        return deckEntity;
    }

    public void setDeckEntity(DeckEntity deckEntity) {
        this.deckEntity = deckEntity;
    }

}
