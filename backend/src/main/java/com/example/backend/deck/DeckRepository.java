package com.example.backend.deck;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.trida.TridaEntity;

public interface DeckRepository extends JpaRepository<DeckEntity, Long> {
    List<DeckEntity> findByTridaEntity(TridaEntity tridaEntity);
}
