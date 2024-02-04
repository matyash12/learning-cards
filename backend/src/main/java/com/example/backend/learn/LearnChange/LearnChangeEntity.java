package com.example.backend.learn.LearnChange;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.backend.card.CardEntity;
import com.example.backend.learn.LearnSession.LearnSessionEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "learnchange")
public class LearnChangeEntity {

    // constructors
    public LearnChangeEntity() {
    }

    public LearnChangeEntity(LearnSessionEntity learnSessionEntity, CardEntity cardEntity) {
        this.learnSessionEntity = learnSessionEntity;
        this.cardEntity = cardEntity;
    }

    // variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LearnSessionEntity learnSessionEntity;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CardEntity cardEntity;

    @CreationTimestamp
    private Instant createdDateTime;

    // functions
    public Instant getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Instant createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LearnSessionEntity getLearnSessionEntity() {
        return learnSessionEntity;
    }

    public void setLearnSessionEntity(LearnSessionEntity learnSessionEntity) {
        this.learnSessionEntity = learnSessionEntity;
    }

    public CardEntity getCardEntity() {
        return cardEntity;
    }

    public void setCardEntity(CardEntity cardEntity) {
        this.cardEntity = cardEntity;
    }

}
