package org.deepdrimz.drimzsport.notification.sdk.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.PlatformType;

/**
 * Request object for registering a device token for push notifications.
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDeviceTokenRequest {

    /**
     * User ID associated with this device.
     */
    @NotBlank(message = "User ID is required")
    private String userId;

    /**
     * Device token from FCM or APNS.
     */
    @NotBlank(message = "Device token is required")
    private String token;

    /**
     * Platform type (ANDROID, IOS, WEB).
     */
    @NotNull(message = "Platform is required")
    private PlatformType platform;

    /**
     * Unique device identifier.
     */
    @NotBlank(message = "Device ID is required")
    private String deviceId;

    /**
     * Application version (optional).
     */
    private String appVersion;
}