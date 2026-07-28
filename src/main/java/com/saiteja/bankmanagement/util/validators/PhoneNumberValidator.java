package com.saiteja.bankmanagement.util.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {

    // Regex breakdown:
    // ^(?:\\+91|91|0)?   -> Optional prefix: +91, 91, or 0
    // [6-9]              -> First digit of the main 10 digits must be 6, 7, 8, or 9
    // \\d{9}$            -> Exactly 9 more digits follow
    private static final String INDIA_PHONE_REGEX = "^(?:\\+91|91|0)?[6-9]\\d{9}$";
    private static final Pattern PATTERN = Pattern.compile(INDIA_PHONE_REGEX);

    /**
     * Validates if the given string is a valid Indian mobile number.
     * Strips common formatting characters (spaces, dashes) before validation.
     * 
     * @param phoneStr The input phone number string
     * @return true if valid, false otherwise
     */
    public static boolean isValidIndianNumber(String phoneStr) {
        if (phoneStr == null || phoneStr.trim().isEmpty()) {
            return false;
        }

        // Clean the string by removing spaces, hyphens, and parentheses
        String cleaned = phoneStr.replaceAll("[\\s\\-()]", "");

        Matcher matcher = PATTERN.matcher(cleaned);
        return matcher.matches();
    }

}

