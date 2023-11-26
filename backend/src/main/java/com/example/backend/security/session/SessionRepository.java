package com.example.backend.security.session;


import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    SessionEntity findByJSESSIONID(String JSESSIONID);
    SessionEntity findFirstByuserIdOrderByCreatedDateTimeDesc(long userId);

} 