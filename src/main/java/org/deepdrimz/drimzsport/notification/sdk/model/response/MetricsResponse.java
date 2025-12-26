package org.deepdrimz.drimzsport.notification.sdk.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Response object containing notification metrics.
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricsResponse {

    private Long totalNotifications;
    private Long sentToday;
    private Long failedToday;
    private Double successRate;
    private Double averageDeliveryTime;
    private Map<String, ChannelMetrics> byChannel;
    private Map<String, Long> byStatus;
}
