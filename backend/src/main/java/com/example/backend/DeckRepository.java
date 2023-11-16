package com.example.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<DeckEntity, Long> {
    List<DeckEntity> findByTridaEntity(TridaEntity tridaEntity);
}
