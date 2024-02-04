package com.example.backend.learn.LearnChange;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.learn.LearnSession.LearnSessionEntity;

public interface LearnChangeRepository extends JpaRepository<LearnChangeEntity, Long> {
    List<LearnChangeEntity> findByLearnSessionEntity(LearnSessionEntity learnSessionEntity);
    List<LearnChangeEntity> findByLearnSessionEntityOrderByIdAsc(LearnSessionEntity learnSessionEntity);

}
