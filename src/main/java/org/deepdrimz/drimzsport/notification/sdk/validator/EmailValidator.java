package org.deepdrimz.drimzsport.notification.sdk.validator;

import java.util.regex.Pattern;

/**
 * Validator for email addresses.
 *
 * @since 1.0.0
 */
public class EmailValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    /**
     * Validates an email address.
     *
     * @param email the email to validate
     * @return true if valid, false otherwise
     */
    public boolean isValid(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates an email address and throws exception if invalid.
     *
     * @param email the email to validate
     * @throws IllegalArgumentException if the email is invalid
     */
    public void validate(String email) {
        if (!isValid(email)) {
            throw new IllegalArgumentException("Invalid email address: " + email);
        }
    }
}
