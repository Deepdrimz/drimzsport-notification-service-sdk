package org.deepdrimz.drimzsport.notification.sdk.model.enums;

/**
 * Enumeration of notification statuses.
 *
 * @since 1.0.0
 */
public enum NotificationStatus {
    /** Notification is pending */
    PENDING,
    /** Notification is being processed */
    PROCESSING,
    /** Notification was successfully sent */
    SENT,
    /** Notification failed to send */
    FAILED,
    /** Notification is scheduled for retry */
    RETRY,
    /** Notification was cancelled */
    CANCELLED
}
