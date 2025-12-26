package org.deepdrimz.drimzsport.notification.sdk.exception;

/**
 * Base exception for all notification client errors.
 *
 * @since 1.0.0
 */
public class NotificationClientException extends RuntimeException {

    public NotificationClientException(String message) {
        super(message);
    }

    public NotificationClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
