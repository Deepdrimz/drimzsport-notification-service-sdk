package org.deepdrimz.drimzsport.notification.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.PlatformType;

import java.time.LocalDateTime;

/**
 * Response object containing device token registration details.
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTokenResponse {

    private String id;
    private String userId;
    private PlatformType platform;
    private String deviceId;
    private boolean active;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registeredAt;
}