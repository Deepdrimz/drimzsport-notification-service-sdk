package org.deepdrimz.drimzsport.notification.sdk.client;

import lombok.extern.slf4j.Slf4j;
import org.deepdrimz.drimzsport.notification.sdk.exception.*;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationChannel;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationPriority;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationType;
import org.deepdrimz.drimzsport.notification.sdk.model.request.BulkNotificationRequest;
import org.deepdrimz.drimzsport.notification.sdk.model.request.SendNotificationRequest;
import org.deepdrimz.drimzsport.notification.sdk.model.response.BulkNotificationResponse;
import org.deepdrimz.drimzsport.notification.sdk.model.response.NotificationResponse;
import org.deepdrimz.drimzsport.notification.sdk.validator.NotificationRequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * Simplified Notification Service Client - Essential Operations Only.
 *
 * <p>This client provides the core operations that typical clients need:
 * <ul>
 *   <li>Send single notifications (Email, SMS, Push)</li>
 *   <li>Send bulk notifications</li>
 *   <li>Check notification status</li>
 * </ul>
 *
 * <h3>Quick Start:</h3>
 * <pre>{@code
 * // Create client
 * NotificationServiceClient client = NotificationServiceClient.builder()
 *     .baseUrl("https://api.drimzsport.com")
 *     .apiKey("your-api-key")
 *     .build();
 *
 * // Send email
 * NotificationResponse response = client.sendEmail(
 *     "user@example.com",
 *     "Welcome to DrimzSport!",
 *     "welcome-template",
 *     Map.of("userName", "John")
 * );
 * }</pre>
 *
 * @author DrimzSport Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
public class NotificationServiceClient {

    private final WebClient webClient;
    private final NotificationRequestValidator validator;
    private final NotificationServiceClientConfig config;

    public NotificationServiceClient(WebClient webClient, NotificationServiceClientConfig config) {
        this.webClient = webClient;
        this.config = config;
        this.validator = new NotificationRequestValidator();
        log.info("Notification Service Client initialized with base URL: {}", config.getBaseUrl());
    }

    public static NotificationServiceClientBuilder builder() {
        return new NotificationServiceClientBuilder();
    }

    // ==================== SEND EMAIL ====================

    /**
     * Sends an email notification.
     *
     * @param email recipient email address
     * @param subject email subject
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendEmail(String email, String subject, String templateId,
                                          java.util.Map<String, Object> variables) {
        return sendEmail(email, subject, templateId, variables, null);
    }

    /**
     * Sends an email notification with priority.
     *
     * @param email recipient email address
     * @param subject email subject
     * @param templateId template identifier
     * @param variables template variables
     * @param priority notification priority (HIGH, NORMAL, LOW, URGENT)
     * @return notification response with ID and status
     */
    public NotificationResponse sendEmail(String email, String subject, String templateId,
                                          java.util.Map<String, Object> variables,
                                          String priority) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.EMAIL_VERIFICATION)
                .channel(NotificationChannel.EMAIL)
                .recipient(email)
                .subject(subject)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(priority != null ?
                        NotificationPriority.valueOf(priority) :
                        NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    // ==================== SEND SMS ====================

    /**
     * Sends an SMS notification.
     *
     * @param phoneNumber recipient phone number (international format: +1234567890)
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendSMS(String phoneNumber, String templateId,
                                        java.util.Map<String, Object> variables) {
        return sendSMS(phoneNumber, templateId, variables, null);
    }

    /**
     * Sends an SMS notification with priority.
     *
     * @param phoneNumber recipient phone number (international format)
     * @param templateId template identifier
     * @param variables template variables
     * @param priority notification priority
     * @return notification response with ID and status
     */
    public NotificationResponse sendSMS(String phoneNumber, String templateId,
                                        java.util.Map<String, Object> variables,
                                        String priority) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.SMS_VERIFICATION)
                .channel(NotificationChannel.SMS)
                .recipient(phoneNumber)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(priority != null ?
                        NotificationPriority.valueOf(priority) :
                        NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    // ==================== SEND PUSH NOTIFICATION ====================

    /**
     * Sends a push notification.
     *
     * @param deviceToken device token (iOS or Android)
     * @param title notification title
     * @param body notification body
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendPush(String deviceToken, String title, String body,
                                         String templateId, java.util.Map<String, Object> variables) {
        return sendPush(deviceToken, title, body, templateId, variables, null, null);
    }

    /**
     * Sends a push notification with custom data.
     *
     * @param deviceToken device token
     * @param title notification title
     * @param body notification body
     * @param templateId template identifier
     * @param variables template variables
     * @param imageUrl optional image URL
     * @param data optional custom data
     * @return notification response with ID and status
     */
    public NotificationResponse sendPush(String deviceToken, String title, String body,
                                         String templateId, java.util.Map<String, Object> variables,
                                         String imageUrl, java.util.Map<String, String> data) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.PUSH_NOTIFICATION)
                .channel(NotificationChannel.PUSH)
                .recipient(deviceToken)
                .title(title)
                .body(body)
                .templateId(templateId)
                .templateVariables(variables)
                .imageUrl(imageUrl)
                .data(data)
                .priority(NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    // ==================== GENERIC SEND ====================

    /**
     * Sends a notification with full control over all parameters.
     *
     * @param request complete notification request
     * @return notification response with ID and status
     */
    public NotificationResponse sendNotification(SendNotificationRequest request) {
        log.debug("Sending notification: type={}, channel={}, recipient={}",
                request.getType(), request.getChannel(), request.getRecipient());

        validator.validate(request);

        return webClient.post()
                .uri("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(NotificationResponse.class)
                .retryWhen(createRetrySpec())
                .onErrorMap(this::mapException)
                .doOnSuccess(response -> log.debug("Notification sent: id={}", response.getId()))
                .block();
    }

    // ==================== BULK OPERATIONS ====================

    /**
     * Sends multiple notifications in bulk.
     *
     * @param requests list of notification requests
     * @return bulk response with batch ID
     */
    public BulkNotificationResponse sendBulk(java.util.List<SendNotificationRequest> requests) {
        log.debug("Sending bulk notifications: count={}", requests.size());

        requests.forEach(validator::validate);

        BulkNotificationRequest bulkRequest = BulkNotificationRequest.builder()
                .notifications(requests)
                .build();

        return webClient.post()
                .uri("/api/v1/notifications/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bulkRequest)
                .retrieve()
                .bodyToMono(BulkNotificationResponse.class)
                .retryWhen(createRetrySpec())
                .onErrorMap(this::mapException)
                .block();
    }

    // ==================== STATUS CHECK ====================

    /**
     * Gets notification status by ID.
     *
     * @param notificationId notification ID
     * @return notification details including status
     */
    public NotificationResponse getStatus(String notificationId) {
        log.debug("Getting notification status: id={}", notificationId);

        return webClient.get()
                .uri("/api/v1/notifications/{id}", notificationId)
                .retrieve()
                .bodyToMono(NotificationResponse.class)
                .onErrorMap(this::mapException)
                .block();
    }

    // ==================== HELPER METHODS ====================

    private Retry createRetrySpec() {
        return Retry.backoff(config.getMaxRetries(), Duration.ofMillis(config.getRetryDelayMillis()))
                .maxBackoff(Duration.ofSeconds(config.getMaxRetryDelaySeconds()))
                .filter(this::isRetryable)
                .doBeforeRetry(retrySignal ->
                        log.warn("Retrying request, attempt: {}", retrySignal.totalRetries() + 1));
    }

    private boolean isRetryable(Throwable throwable) {
        if (throwable instanceof WebClientResponseException ex) {
            HttpStatus status = (HttpStatus) ex.getStatusCode();
            return status.is5xxServerError() || status == HttpStatus.REQUEST_TIMEOUT;
        }
        return false;
    }

    private Throwable mapException(Throwable throwable) {
        if (throwable instanceof WebClientResponseException ex) {
            HttpStatus status = (HttpStatus) ex.getStatusCode();
            String message = ex.getResponseBodyAsString();

            return switch (status) {
                case NOT_FOUND -> new NotificationNotFoundException(message);
                case BAD_REQUEST -> new ValidationException(message);
                case UNAUTHORIZED -> new AuthenticationException("Invalid or missing API key");
                case TOO_MANY_REQUESTS -> new RateLimitExceededException("Rate limit exceeded");
                case SERVICE_UNAVAILABLE, BAD_GATEWAY, GATEWAY_TIMEOUT ->
                        new ServiceUnavailableException(message);
                default -> new NotificationClientException(message, ex);
            };
        }
        return new NotificationClientException("Unexpected error", throwable);
    }
}