package org.deepdrimz.drimzsport.notification.sdk.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationChannel;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationPriority;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationType;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Request object for sending a single notification.
 *
 * <p>This class is immutable and thread-safe. Use the builder pattern to construct instances.</p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * SendNotificationRequest request = SendNotificationRequest.builder()
 *     .type(NotificationType.EMAIL_VERIFICATION)
 *     .channel(NotificationChannel.EMAIL)
 *     .recipient("user@example.com")
 *     .templateId("email-verification-template")
 *     .templateVariables(Map.of("userName", "John", "code", "123456"))
 *     .priority(NotificationPriority.HIGH)
 *     .build();
 * }</pre>
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {

    /**
     * The type of notification to send.
     */
    @NotNull(message = "Notification type is required")
    private NotificationType type;

    /**
     * The channel through which to send the notification.
     */
    @NotNull(message = "Channel is required")
    private NotificationChannel channel;

    /**
     * The recipient of the notification (email, phone number, or device token).
     */
    @NotBlank(message = "Recipient is required")
    private String recipient;

    /**
     * The ID of the template to use for rendering the notification.
     */
    @NotBlank(message = "Template ID is required")
    private String templateId;

    /**
     * Variables to be interpolated into the template.
     */
    private Map<String, Object> templateVariables;

    /**
     * The priority of the notification (default: NORMAL).
     */
    private NotificationPriority priority;

    /**
     * The scheduled time for sending the notification (optional).
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduledAt;

    // Email-specific fields
    /**
     * Email subject (required for email notifications).
     */
    private String subject;

    /**
     * CC recipients for email notifications.
     */
    private List<String> ccRecipients;

    /**
     * BCC recipients for email notifications.
     */
    private List<String> bccRecipients;

    /**
     * Attachment URLs for email notifications.
     */
    private List<String> attachments;

    // Push notification-specific fields
    /**
     * Title for push notifications.
     */
    private String title;

    /**
     * Body text for push notifications.
     */
    private String body;

    /**
     * Custom data for push notifications.
     */
    private Map<String, String> data;

    /**
     * Image URL for push notifications.
     */
    private String imageUrl;

    /**
     * Click action for push notifications.
     */
    private String clickAction;

    // SMS-specific fields
    /**
     * SMS provider name (optional, auto-selected if not provided).
     */
    private String provider;
}
