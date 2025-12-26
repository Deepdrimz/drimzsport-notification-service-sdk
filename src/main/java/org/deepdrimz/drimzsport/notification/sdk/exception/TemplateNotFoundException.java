package org.deepdrimz.drimzsport.notification.sdk.exception;

/**
 * Exception thrown when a template is not found.
 *
 * @since 1.0.0
 */
public class TemplateNotFoundException extends NotificationClientException {

    public TemplateNotFoundException(String message) {
        super(message);
    }
}
