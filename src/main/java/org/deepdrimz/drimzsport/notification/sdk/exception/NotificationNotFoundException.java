package org.deepdrimz.drimzsport.notification.sdk.exception;

/**
 * Exception thrown when a notification is not found.
 *
 * @since 1.0.0
 */
public class NotificationNotFoundException extends NotificationClientException {

    public NotificationNotFoundException(String message) {
        super(message);
    }

}
