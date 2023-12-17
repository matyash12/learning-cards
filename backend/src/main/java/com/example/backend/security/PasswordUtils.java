package com.example.backend.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.stereotype.Component;
@Component
public class PasswordUtils {
    public String generateSaltAsString() {
        byte[] salt = generateSalt();
        return Base64.getEncoder().encodeToString(salt);
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public String hashPassword(String password, String salt) {
        int iterations = 10000;
        int keyLength = 256;

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, keyLength);

        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hashedBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            // Handle the exception appropriately
            return null;
        }
    }

    public boolean verifyPassword(String enteredPassword, String salt, String storedHash) {
        String enteredPasswordHash = hashPassword(enteredPassword, salt);
        return enteredPasswordHash != null && enteredPasswordHash.equals(storedHash);
    }
}




