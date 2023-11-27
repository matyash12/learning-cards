package com.example.backend.user;
import java.util.regex.Pattern;

public class UsernameChecker {


    public static boolean isUsernameStrong(String username) {
        // Check length
        if (username.length() < 5) {
            System.out.println("Username must be at least 5 characters long.");
            return false;
        }

        // Check for alphanumeric characters only
        if (!username.matches("[a-zA-Z0-9]+")) {
            System.out.println("Username must contain only alphanumeric characters.");
            return false;
        }

        // Additional checks can be added based on your requirements

        // If all checks pass, the username is considered strong
        return true;
    }

    public static String getUsernameRequirementsMessage() {
        return "Please create a strong username that meets the following criteria:\n" +
                "- Minimum length of 5 characters.\n" +
                "- Should contain only alphanumeric characters (letters A-Z, a-z, and digits 0-9).";
    }
}