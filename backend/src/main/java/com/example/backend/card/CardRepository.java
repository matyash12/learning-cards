package com.example.backend.card;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.deck.DeckEntity;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByDeckEntity(DeckEntity deckEntity);

    @Query(value = "SELECT COUNT(cards.mark) AS count FROM (SELECT 0 AS mark UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5) AS marks LEFT JOIN cards ON marks.mark = cards.mark AND cards.deck_entity_id = :deckEntityId GROUP BY marks.mark", nativeQuery = true)
    Double[] TableMarksByDeckEntityId(@Param("deckEntityId") Long deckEntityId);
}