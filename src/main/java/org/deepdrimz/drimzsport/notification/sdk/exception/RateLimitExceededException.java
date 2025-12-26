package org.deepdrimz.drimzsport.notification.sdk.exception;

/**
 * Exception thrown when a notification service encounter a rate limit exceeded exception.
 *
 * @since 1.0.0
 */
public class RateLimitExceededException extends NotificationClientException {

    public RateLimitExceededException(String message) {
        super(message);
    }

}