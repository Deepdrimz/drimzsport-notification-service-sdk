package org.deepdrimz.drimzsport.notification.sdk.model.enums;

/**
 * Enumeration of supported notification types.
 *
 * <p>Each notification type corresponds to a specific use case and default channel.</p>
 *
 * @since 1.0.0
 */
public enum NotificationType {
    /** Email verification notification */
    EMAIL_VERIFICATION,
    /** Password reset notification */
    PASSWORD_RESET,
    /** Match ticket availability notification */
    MATCH_TICKET_AVAILABLE,
    /** Subscription expiring warning */
    SUBSCRIPTION_EXPIRING,
    /** Promotional offer notification */
    PROMOTIONAL_OFFER,
    /** Welcome email */
    WELCOME_EMAIL,
    /** Transaction receipt */
    TRANSACTION_RECEIPT,

    /** SMS verification code */
    SMS_VERIFICATION,
    /** Security alert via SMS */
    SMS_SECURITY_ALERT,
    /** SMS promotional offer */
    SMS_PROMOTIONAL_OFFER,
    /** Match reminder via SMS */
    SMS_MATCH_REMINDER,
    /** Subscription reminder via SMS */
    SMS_SUBSCRIPTION_REMINDER,
    /** Transaction alert via SMS */
    SMS_TRANSACTION_ALERT,

    /** Generic push notification */
    PUSH_NOTIFICATION,
    /** Match update push notification */
    PUSH_MATCH_UPDATE,
    /** Bet update push notification */
    PUSH_BET_UPDATE,
    /** Promotional push notification */
    PUSH_PROMOTIONAL
}
