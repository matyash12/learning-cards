package com.example.backend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<DeckEntity, Long> {

}
