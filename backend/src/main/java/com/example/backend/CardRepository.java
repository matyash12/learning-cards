package com.example.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByDeckEntity(DeckEntity deckEntity);
}