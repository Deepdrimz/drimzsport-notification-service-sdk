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

    PUSH_NOTIFICATION(NotificationChannel.PUSH, "Generic Push Notification", null),

    /* ===================== ACCOUNT & AUTH ===================== */

    /** Email verification during registration or email change */
    EMAIL_VERIFICATION(NotificationChannel.EMAIL, "Email Verification", "notifications"),

    /** Password reset request notification */
    PASSWORD_RESET(NotificationChannel.EMAIL, "Password Reset", "notifications"),

    /** Welcome email sent after successful registration */
    WELCOME_EMAIL(NotificationChannel.EMAIL, "Welcome Email", "notifications"),

    /* ===================== TRANSACTIONS ===================== */

    /** Transaction receipt or payment confirmation */
    TRANSACTION_RECEIPT(NotificationChannel.EMAIL, "Transaction Receipt", "notifications"),

    /** Transaction alert delivered via SMS */
    SMS_TRANSACTION_ALERT(NotificationChannel.SMS, "Transaction Alert", null),

    /* ===================== SUBSCRIPTIONS & PROMOS ===================== */

    /** Subscription expiration warning */
    SUBSCRIPTION_EXPIRING(NotificationChannel.EMAIL, "Subscription Expiring", "notifications"),

    /** Promotional or marketing offer via email */
    PROMOTIONAL_OFFER(NotificationChannel.EMAIL, "Promotional Offer", "marketing"),

    /** Promotional offer delivered via SMS */
    SMS_PROMOTIONAL_OFFER(NotificationChannel.SMS, "Promotional Offer", null),

    /** Promotional notification delivered via push */
    PUSH_PROMOTIONAL(NotificationChannel.PUSH, "Promotional Notification", null),

    /* ===================== SPORTS & BETTING ===================== */

    /** Notification when match tickets become available */
    MATCH_TICKET_AVAILABLE(NotificationChannel.EMAIL, "Match Ticket Available", "marketing"),

    /** Match reminder sent via SMS */
    SMS_MATCH_REMINDER(NotificationChannel.SMS, "Match Reminder", null),

    /** Match update delivered via push notification */
    PUSH_MATCH_UPDATE(NotificationChannel.PUSH, "Match Update", null),

    /** Betting-related update delivered via push */
    PUSH_BET_UPDATE(NotificationChannel.PUSH, "Bet Update", null),

    /* ===================== SECURITY ===================== */

    /** One-time verification code sent via SMS */
    SMS_VERIFICATION(NotificationChannel.SMS, "SMS Verification", null),

    /** Security alert for suspicious account activity */
    SMS_SECURITY_ALERT(NotificationChannel.SMS, "Security Alert", null),

    /* ===================== KYC – USER ===================== */

    /** KYC documents successfully submitted */
    KYC_SUBMITTED(NotificationChannel.EMAIL, "KYC Submitted", "notifications"),

    /** KYC verification approved */
    KYC_APPROVED(NotificationChannel.EMAIL, "KYC Approved", "notifications"),

    /** KYC verification rejected with reason */
    KYC_REJECTED(NotificationChannel.EMAIL, "KYC Rejected", "notifications"),

    /** User must resubmit KYC documents */
    KYC_RESUBMISSION_REQUIRED(NotificationChannel.EMAIL, "KYC Resubmission Required", "notifications"),

    /** KYC document is nearing expiration */
    KYC_DOCUMENT_EXPIRING(NotificationChannel.EMAIL, "KYC Document Expiring", "notifications"),

    /* ===================== KYC – ADMIN / COMPLIANCE ===================== */

    /** KYC case requires manual review by compliance */
    KYC_REVIEW_REQUIRED(NotificationChannel.EMAIL, "KYC Review Required", "support"),

    /** Manual KYC verification required */
    KYC_MANUAL_VERIFICATION(NotificationChannel.EMAIL, "KYC Manual Verification", "support"),

    /** SLA breach occurred during KYC processing */
    KYC_SLA_BREACH(NotificationChannel.EMAIL, "KYC SLA Breach", "support"),

    /* ===================== SYSTEM & MAINTENANCE ===================== */

    /** Scheduled system maintenance notification */
    SYSTEM_MAINTENANCE_SCHEDULED(NotificationChannel.EMAIL, "Scheduled Maintenance", "notifications"),

    /** System maintenance has started */
    SYSTEM_MAINTENANCE_STARTED(NotificationChannel.PUSH, "Maintenance Started", null),

    /** System maintenance completed successfully */
    SYSTEM_MAINTENANCE_COMPLETED(NotificationChannel.PUSH, "Maintenance Completed", null),

    /** Unexpected system outage notification */
    SYSTEM_OUTAGE(NotificationChannel.SMS, "System Outage", null),

    /** System recovery notification after outage */
    SYSTEM_RECOVERY(NotificationChannel.SMS, "System Recovery", null);

    private final NotificationChannel defaultChannel;
    private final String displayName;
    private final String suggestedEmailAccount; // NEW: Suggested email account name

    NotificationType(NotificationChannel defaultChannel, String displayName, String suggestedEmailAccount) {
        this.defaultChannel = defaultChannel;
        this.displayName = displayName;
        this.suggestedEmailAccount = suggestedEmailAccount;
    }

    public NotificationChannel getDefaultChannel() {
        return defaultChannel;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the suggested email account name for this notification type.
     * Returns null for non-email notifications or if no specific account is suggested.
     *
     * @return suggested email account name (e.g., "marketing", "support", "notifications") or null
     */
    public String getSuggestedEmailAccount() {
        return suggestedEmailAccount;
    }

    /**
     * Checks if this notification type has a suggested email account
     */
    public boolean hasSuggestedEmailAccount() {
        return suggestedEmailAccount != null && !suggestedEmailAccount.isBlank();
    }
}