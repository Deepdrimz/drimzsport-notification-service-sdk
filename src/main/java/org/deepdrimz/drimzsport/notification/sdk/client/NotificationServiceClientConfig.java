package org.deepdrimz.drimzsport.notification.sdk.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * Configuration class for the Notification Service Client.
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationServiceClientConfig {

    /**
     * Base URL of the notification service API.
     */
    private String baseUrl;

    /**
     * API key for authentication (optional).
     */
    private String apiKey;

    /**
     * Connection timeout in milliseconds (default: 5000).
     */
    @Builder.Default
    private long connectionTimeoutMillis = 5000;

    /**
     * Read timeout in milliseconds (default: 30000).
     */
    @Builder.Default
    private long readTimeoutMillis = 30000;

    /**
     * Maximum number of retry attempts (default: 3).
     */
    @Builder.Default
    private int maxRetries = 3;

    /**
     * Initial retry delay in milliseconds (default: 1000).
     */
    @Builder.Default
    private long retryDelayMillis = 1000;

    /**
     * Maximum retry delay in seconds (default: 10).
     */
    @Builder.Default
    private int maxRetryDelaySeconds = 10;

    /**
     * Enable request/response logging (default: false).
     */
    @Builder.Default
    private boolean loggingEnabled = false;
}
