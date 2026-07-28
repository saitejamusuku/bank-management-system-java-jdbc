package com.saiteja.bankmanagement.util.validators;
import java.util.regex.Pattern;



public class EmailValidator {

    // RFC 5322 compiled regex pattern for fast execution
    private static final Pattern EMAIL_REGEX = Pattern.compile(
        "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*" +
        "@gmail.com"
    );

    /**
     * Validates if the given string matches a proper email format.
     * @param email The email string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_REGEX.matcher(email).matches();
    }

}

