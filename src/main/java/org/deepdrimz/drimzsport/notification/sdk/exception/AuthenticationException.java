package org.deepdrimz.drimzsport.notification.sdk.exception;

/**
 * Exception thrown when a notification have authentication problem.
 *
 * @since 1.0.0
 */
public class AuthenticationException extends NotificationClientException {

    public AuthenticationException(String message) {
        super(message);
    }

}

