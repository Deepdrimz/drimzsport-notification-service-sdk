package org.deepdrimz.drimzsport.notification.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationChannel;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationPriority;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationStatus;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response object containing notification details.
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private String id;
    private NotificationType type;
    private NotificationChannel channel;
    private String recipient;
    private NotificationStatus status;
    private NotificationPriority priority;
    private Integer retryCount;
    private String errorMessage;
    private String providerName;
    private String templateId;
    private Map<String, Object> templateVariables;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sentAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduledAt;
}
