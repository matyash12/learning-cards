package com.example.backend.learn.LearnSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LearnSessionRepository extends JpaRepository<LearnSessionEntity, Long> {

    @Query(value = "SELECT (100 - (100 / (COUNT(*) * 4) * SUM(CASE WHEN c.mark = 0 THEN 4 WHEN c.mark = 1 THEN 0 WHEN c.mark = 2 THEN 1 WHEN c.mark = 3 THEN 2 WHEN c.mark = 4 THEN 3 WHEN c.mark = 5 THEN 4 END))) AS test FROM learnsession l JOIN cards c ON c.deck_entity_id = l.deck_entity_id WHERE l.id = :learnSessionId", nativeQuery = true)
    Double findProgressByLearnSessionId(@Param("learnSessionId") Long learnSessionId);
}
