package com.example.backend.recovery;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.backend.security.user.UserEntity;

@Component
public class PasswordResetLinkGenerator {

    @Value("${recovery.password.link}")
    private String recoveryPasswordLink;


    public String generatePasswordResetLink(String token,UserEntity userEntity) {



        // Construct the password reset link
        String resetLink = recoveryPasswordLink  + token + "&userId=" + userEntity.getId().toString();



        return resetLink;
    }

    public String generateRecoveryToken(){
        return UUID.randomUUID().toString();
    }
}
