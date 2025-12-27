package org.deepdrimz.drimzsport.notification.sdk.model.enums;

/**
 * Enumeration of supported notification types.
 *
 * <p>
 * This enum mirrors the notification types defined in the
 * Notification Service domain model. It acts as a strict
 * contract between producers and the notification platform.
 * </p>
 *
 * <p>
 * ⚠ IMPORTANT:
 * Any change to notification types in the notification service
 * MUST be reflected here to maintain type safety and compatibility.
 * </p>
 *
 * @since 1.0.0
 */
public enum NotificationType {

    /* ===================== ACCOUNT & AUTH ===================== */

    /** Email verification during registration or email change */
    EMAIL_VERIFICATION,

    /** Password reset request notification */
    PASSWORD_RESET,

    /** Welcome email sent after successful registration */
    WELCOME_EMAIL,


    /* ===================== TRANSACTIONS ===================== */

    /** Transaction receipt or payment confirmation */
    TRANSACTION_RECEIPT,

    /** Transaction alert delivered via SMS */
    SMS_TRANSACTION_ALERT,


    /* ===================== SUBSCRIPTIONS & PROMOS ===================== */

    /** Subscription expiration warning */
    SUBSCRIPTION_EXPIRING,

    /** Promotional or marketing offer via email */
    PROMOTIONAL_OFFER,

    /** Promotional offer delivered via SMS */
    SMS_PROMOTIONAL_OFFER,

    /** Promotional notification delivered via push */
    PUSH_PROMOTIONAL,


    /* ===================== SPORTS & BETTING ===================== */

    /** Notification when match tickets become available */
    MATCH_TICKET_AVAILABLE,

    /** Match reminder sent via SMS */
    SMS_MATCH_REMINDER,

    /** Match update delivered via push notification */
    PUSH_MATCH_UPDATE,

    /** Betting-related update delivered via push */
    PUSH_BET_UPDATE,


    /* ===================== SECURITY ===================== */

    /** One-time verification code sent via SMS */
    SMS_VERIFICATION,

    /** Security alert for suspicious account activity */
    SMS_SECURITY_ALERT,


    /* ===================== KYC – USER ===================== */

    /** KYC documents successfully submitted */
    KYC_SUBMITTED,

    /** KYC verification approved */
    KYC_APPROVED,

    /** KYC verification rejected with reason */
    KYC_REJECTED,

    /** User must resubmit KYC documents */
    KYC_RESUBMISSION_REQUIRED,

    /** KYC document is nearing expiration */
    KYC_DOCUMENT_EXPIRING,


    /* ===================== KYC – ADMIN / COMPLIANCE ===================== */

    /** KYC case requires manual review by compliance */
    KYC_REVIEW_REQUIRED,

    /** Manual KYC verification required */
    KYC_MANUAL_VERIFICATION,

    /** SLA breach occurred during KYC processing */
    KYC_SLA_BREACH,


    /* ===================== SYSTEM & MAINTENANCE ===================== */

    /** Scheduled system maintenance notification */
    SYSTEM_MAINTENANCE_SCHEDULED,

    /** System maintenance has started */
    SYSTEM_MAINTENANCE_STARTED,

    /** System maintenance completed successfully */
    SYSTEM_MAINTENANCE_COMPLETED,

    /** Unexpected system outage notification */
    SYSTEM_OUTAGE,

    /** System recovery notification after outage */
    SYSTEM_RECOVERY
}
