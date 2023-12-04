package com.example.backend.security.user;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, Long> {    
     UserEntity findByUsername(String username);
} 