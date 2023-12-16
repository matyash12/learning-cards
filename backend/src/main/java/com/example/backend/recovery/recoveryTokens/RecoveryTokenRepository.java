package com.example.backend.recovery.recoveryTokens;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecoveryTokenRepository extends JpaRepository<RecoveryTokenEntity,Long> {
    RecoveryTokenEntity findByToken(String token);
}
