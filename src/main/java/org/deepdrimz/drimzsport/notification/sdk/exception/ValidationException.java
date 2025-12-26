package org.deepdrimz.drimzsport.notification.sdk.exception;

/**
 * Exception thrown when request validation fails.
 *
 * @since 1.0.0
 */
public class ValidationException extends NotificationClientException {

    public ValidationException(String message) {
        super(message);
    }
}
