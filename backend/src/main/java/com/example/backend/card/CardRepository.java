package com.example.backend.card;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.deck.DeckEntity;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByDeckEntity(DeckEntity deckEntity);
}