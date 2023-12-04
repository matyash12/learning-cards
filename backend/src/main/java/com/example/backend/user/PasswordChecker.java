package com.example.backend.user;

public class PasswordChecker {
    public static String getPasswordRequirementsMessage() {
        return "Please create a strong password that meets the following criteria:\n" +
                "- Minimum length of 8 characters.\n" +
                "- At least one digit (0-9).\n" +
                "- At least one uppercase letter (A-Z).\n" +
                "- At least one lowercase letter (a-z).";
    }

    public static boolean isPasswordStrong(String password) {
        // Check length
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }

        // Check for at least one digit
        if (!password.matches(".*\\d.*")) {
            System.out.println("Password must contain at least one digit.");
            return false;
        }

        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            System.out.println("Password must contain at least one uppercase letter.");
            return false;
        }

        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            System.out.println("Password must contain at least one lowercase letter.");
            return false;
        }

        // // Check for at least one special character
        // Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9]");
        // Matcher matcher = specialCharPattern.matcher(password);
        // if (!matcher.find()) {
        //     System.out.println("Password must contain at least one special character.");
        //     return false;
        // }

        // If all checks pass, the password is considered strong
        return true;
    }
}