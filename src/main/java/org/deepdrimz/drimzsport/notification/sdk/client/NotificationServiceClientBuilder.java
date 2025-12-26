package org.deepdrimz.drimzsport.notification.sdk.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Builder for creating NotificationServiceClient instances.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * NotificationServiceClient client = NotificationServiceClient.builder()
 *     .baseUrl("https://api.drimzsport.com")
 *     .apiKey("your-api-key")
 *     .connectionTimeout(Duration.ofSeconds(5))
 *     .readTimeout(Duration.ofSeconds(30))
 *     .maxRetries(3)
 *     .enableLogging(true)
 *     .build();
 * }</pre>
 *
 * @since 1.0.0
 */
public class NotificationServiceClientBuilder {

    private final NotificationServiceClientConfig config;

    public NotificationServiceClientBuilder() {
        this.config = NotificationServiceClientConfig.builder().build();
    }

    /**
     * Sets the base URL of the notification service.
     *
     * @param baseUrl the base URL (e.g., "https://api.drimzsport.com")
     * @return this builder
     */
    public NotificationServiceClientBuilder baseUrl(String baseUrl) {
        config.setBaseUrl(baseUrl);
        return this;
    }

    /**
     * Sets the API key for authentication.
     *
     * @param apiKey the API key
     * @return this builder
     */
    public NotificationServiceClientBuilder apiKey(String apiKey) {
        config.setApiKey(apiKey);
        return this;
    }

    /**
     * Sets the connection timeout.
     *
     * @param timeout the connection timeout
     * @return this builder
     */
    public NotificationServiceClientBuilder connectionTimeout(Duration timeout) {
        config.setConnectionTimeoutMillis(timeout.toMillis());
        return this;
    }

    /**
     * Sets the read timeout.
     *
     * @param timeout the read timeout
     * @return this builder
     */
    public NotificationServiceClientBuilder readTimeout(Duration timeout) {
        config.setReadTimeoutMillis(timeout.toMillis());
        return this;
    }

    /**
     * Sets the maximum number of retry attempts.
     *
     * @param maxRetries the maximum retries
     * @return this builder
     */
    public NotificationServiceClientBuilder maxRetries(int maxRetries) {
        config.setMaxRetries(maxRetries);
        return this;
    }

    /**
     * Enables request/response logging.
     *
     * @param enabled true to enable logging
     * @return this builder
     */
    public NotificationServiceClientBuilder enableLogging(boolean enabled) {
        config.setLoggingEnabled(enabled);
        return this;
    }

    /**
     * Builds the NotificationServiceClient with the configured settings.
     *
     * @return a new NotificationServiceClient instance
     */
    public NotificationServiceClient build() {
        validateConfig();

        HttpClient httpClient = createHttpClient();
        WebClient webClient = createWebClient(httpClient);

        return new NotificationServiceClient(webClient, config);
    }

    private void validateConfig() {
        if (config.getBaseUrl() == null || config.getBaseUrl().isBlank()) {
            throw new IllegalArgumentException("Base URL is required");
        }
    }

    private HttpClient createHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) config.getConnectionTimeoutMillis())
                .responseTimeout(Duration.ofMillis(config.getReadTimeoutMillis()))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(config.getReadTimeoutMillis(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(config.getReadTimeoutMillis(), TimeUnit.MILLISECONDS)));
    }

    private WebClient createWebClient(HttpClient httpClient) {
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(config.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient));

        // Add API key header if provided
        if (config.getApiKey() != null && !config.getApiKey().isBlank()) {
            builder.defaultHeader("X-API-Key", config.getApiKey());
        }

        // Add logging filter if enabled
        if (config.isLoggingEnabled()) {
            builder.filter(logRequest());
            builder.filter(logResponse());
        }

        return builder.build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            System.out.println("Response: " + clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}

