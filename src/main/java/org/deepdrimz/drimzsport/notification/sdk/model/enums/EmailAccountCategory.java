package org.deepdrimz.drimzsport.notification.sdk.model.enums;

/**
 * Enumeration of logical email account categories.
 *
 * <p>
 * Email account categories define the <b>sender identity</b> used when delivering
 * email notifications. Multiple notification types may map to the same category.
 * </p>
 *
 * <p>
 * This abstraction allows the notification platform to dynamically resolve
 * the correct email sender (SMTP account, from address, reply-to behavior)
 * without clients needing to know implementation details.
 * </p>
 *
 * <h3>Examples:</h3>
 * <ul>
 *   <li>{@code NOTIFICATIONS} → noreply@drimzsport.com (system emails)</li>
 *   <li>{@code MARKETING} → marketing@drimzsport.com (promotions)</li>
 *   <li>{@code SUPPORT} → support@drimzsport.com (reply-enabled)</li>
 * </ul>
 *
 * <p>
 * ⚠ This enum must remain stable. Adding new values is allowed,
 * but renaming or removing values is a breaking change.
 * </p>
 *
 * @since 1.0.4
 */
public enum EmailAccountCategory {

    /**
     * System and transactional emails.
     *
     * <p>
     * Typically sent from a no-reply address.
     * Used for authentication, KYC, receipts, and system alerts.
     * </p>
     */
    NOTIFICATIONS,

    /**
     * Marketing and promotional emails.
     *
     * <p>
     * Used for campaigns, offers, announcements, and user engagement.
     * Usually sent from a no-reply or campaign-specific address.
     * </p>
     */
    MARKETING,

    /**
     * Customer support and compliance emails.
     *
     * <p>
     * Reply-enabled sender used for customer communication,
     * KYC reviews, and support workflows.
     * </p>
     */
    SUPPORT,

    /**
     * Informational emails.
     *
     * <p>
     * Used for general information, policy updates, and announcements
     * that are not strictly transactional or marketing-related.
     * </p>
     */
    INFORMATIONAL
}

