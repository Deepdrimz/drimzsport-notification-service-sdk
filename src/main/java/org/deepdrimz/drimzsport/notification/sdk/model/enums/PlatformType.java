package org.deepdrimz.drimzsport.notification.sdk.model.enums;

/**
 * Enumeration of supported mobile platforms for push notifications.
 *
 * @since 1.0.0
 */
public enum PlatformType {
    /** Android platform using Firebase Cloud Messaging */
    ANDROID,

    /** iOS platform using Apple Push Notification Service */
    IOS,

    /** Web platform using Firebase Cloud Messaging */
    WEB
}