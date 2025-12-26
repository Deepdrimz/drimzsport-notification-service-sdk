package org.deepdrimz.drimzsport.notification.sdk.validator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Validator for phone numbers using Google's libphonenumber.
 *
 * @since 1.0.0
 */
public class PhoneNumberValidator {

    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    /**
     * Validates a phone number.
     *
     * @param phoneNumber the phone number to validate (in international format)
     * @return true if valid, false otherwise
     */
    public boolean isValid(String phoneNumber) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, null);
            return phoneUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }
    }

    /**
     * Validates a phone number and throws exception if invalid.
     *
     * @param phoneNumber the phone number to validate
     * @throws IllegalArgumentException if the phone number is invalid
     */
    public void validate(String phoneNumber) {
        if (!isValid(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number: " + phoneNumber);
        }
    }
}
