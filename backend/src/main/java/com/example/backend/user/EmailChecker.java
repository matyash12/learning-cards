package com.example.backend.user;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailChecker {
    public static boolean isEmailValid(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
    public static String getEmailValidationMessage(String email){
        if (email == null || email.isEmpty()) {
            return "Email address is required.";
        }
    
        // You can add more validation criteria here based on your requirements.
    
        // Example: Check if the email contains the "@" symbol
        if (!email.contains("@")) {
            return "Invalid email format. Missing '@' symbol.";
        }
    
        // Add more validation criteria as needed.
    
        // If the email passes all validations, return a success message
        return "Email address is valid.";
    }
}
