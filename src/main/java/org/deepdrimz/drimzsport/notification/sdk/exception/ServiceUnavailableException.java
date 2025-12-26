package org.deepdrimz.drimzsport.notification.sdk.exception;

/**
 * Exception thrown when the notification service is unavailable.
 *
 * @since 1.0.0
 */
public class ServiceUnavailableException extends NotificationClientException {

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
