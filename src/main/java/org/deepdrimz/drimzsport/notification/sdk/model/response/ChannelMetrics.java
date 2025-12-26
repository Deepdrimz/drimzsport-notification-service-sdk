package org.deepdrimz.drimzsport.notification.sdk.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Metrics for a specific notification channel.
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMetrics {
    private Long sent;
    private Long failed;
    private Double successRate;
}
