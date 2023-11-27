package com.example.backend.trida;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.security.user.UserEntity;

public interface TridaRepository extends JpaRepository<TridaEntity, Long> {
        List<TridaEntity> findAllByUserEntity(UserEntity userEntity);

}
