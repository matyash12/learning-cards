package com.example.backend.images;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.card.CardEntity;


public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByCardEntity(CardEntity cardEntity);
}
