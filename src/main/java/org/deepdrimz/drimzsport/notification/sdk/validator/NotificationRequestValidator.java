package org.deepdrimz.drimzsport.notification.sdk.validator;

import org.deepdrimz.drimzsport.notification.sdk.exception.ValidationException;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationChannel;
import org.deepdrimz.drimzsport.notification.sdk.model.request.SendNotificationRequest;

/**
 * Validator for notification requests.
 *
 * @since 1.0.0
 */
public class NotificationRequestValidator {

    private final EmailValidator emailValidator = new EmailValidator();
    private final PhoneNumberValidator phoneValidator = new PhoneNumberValidator();

    /**
     * Validates a notification request.
     *
     * @param request the request to validate
     * @throws ValidationException if validation fails
     */
    public void validate(SendNotificationRequest request) {
        if (request == null) {
            throw new ValidationException("Request cannot be null");
        }

        if (request.getType() == null) {
            throw new ValidationException("Notification type is required");
        }

        if (request.getChannel() == null) {
            throw new ValidationException("Channel is required");
        }

        if (request.getRecipient() == null || request.getRecipient().isBlank()) {
            throw new ValidationException("Recipient is required");
        }

        if (request.getTemplateId() == null || request.getTemplateId().isBlank()) {
            throw new ValidationException("Template ID is required");
        }

        // Validate recipient format based on channel
        validateRecipient(request.getChannel(), request.getRecipient());

        // Channel-specific validations
        if (request.getChannel() == NotificationChannel.EMAIL) {
            if (request.getSubject() == null || request.getSubject().isBlank()) {
                throw new ValidationException("Subject is required for email notifications");
            }
        }

        if (request.getChannel() == NotificationChannel.PUSH) {
            if (request.getTitle() == null || request.getTitle().isBlank()) {
                throw new ValidationException("Title is required for push notifications");
            }
            if (request.getBody() == null || request.getBody().isBlank()) {
                throw new ValidationException("Body is required for push notifications");
            }
        }
    }

    private void validateRecipient(NotificationChannel channel, String recipient) {
        switch (channel) {
            case EMAIL -> {
                if (!emailValidator.isValid(recipient)) {
                    throw new ValidationException("Invalid email address: " + recipient);
                }
            }
            case SMS -> {
                if (!phoneValidator.isValid(recipient)) {
                    throw new ValidationException("Invalid phone number: " + recipient);
                }
            }
            case PUSH -> {
                // For push notifications, recipient can be either:
                // 1. User ID (recommended) - any non-empty string
                // 2. Device token (legacy) - must be at least 20 characters
                if (recipient.length() < 3) {
                    throw new ValidationException("Invalid recipient: must be user ID or device token");
                }
                // Note: User IDs are typically shorter than device tokens
                // Both are accepted here for backward compatibility
            }
        }
    }
}