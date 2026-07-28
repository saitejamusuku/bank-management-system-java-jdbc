package com.saiteja.bankmanagement.util.validators;

public class PasswordValidator {

    /**
     * Validates that the input is not null, not empty, and does not consist 
     * solely of whitespace. It also blocks leading/trailing spaces.
     * 
     * @param password The input password string
     * @return true if valid, false if empty or whitespace-only
     */
    public static boolean isValidInput(String password) {
        // Fail if null, completely empty, or starts/ends with a space
        if (password == null || password.isEmpty() || password.startsWith(" ") || password.endsWith(" ")) {
            return false;
        }
        
        // Fail if the string is made up entirely of spaces/tabs
        return !password.trim().isEmpty();
    }

   
}
