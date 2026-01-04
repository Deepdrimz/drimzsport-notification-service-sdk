package org.deepdrimz.drimzsport.notification.sdk.client;

import lombok.extern.slf4j.Slf4j;
import org.deepdrimz.drimzsport.notification.sdk.exception.*;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationChannel;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationPriority;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationType;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.PlatformType;
import org.deepdrimz.drimzsport.notification.sdk.model.request.BulkNotificationRequest;
import org.deepdrimz.drimzsport.notification.sdk.model.request.RegisterDeviceTokenRequest;
import org.deepdrimz.drimzsport.notification.sdk.model.request.SendNotificationRequest;
import org.deepdrimz.drimzsport.notification.sdk.model.response.BulkNotificationResponse;
import org.deepdrimz.drimzsport.notification.sdk.model.response.DeviceTokenResponse;
import org.deepdrimz.drimzsport.notification.sdk.model.response.NotificationResponse;
import org.deepdrimz.drimzsport.notification.sdk.validator.NotificationRequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

/**
 * Notification Service Client with Multi-Email Account Support.
 *
 * <p>This client provides core notification operations with intelligent email routing:
 * <ul>
 *   <li>Send single notifications (Email, SMS, Push)</li>
 *   <li>Send bulk notifications</li>
 *   <li>Device token management for push notifications</li>
 *   <li>Check notification status</li>
 *   <li>Multi-email account support (marketing, support, notifications)</li>
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
 * // Send marketing email (automatically uses marketing@drimzsport.com)
 * NotificationResponse response = client.sendMarketingEmail(
 *     "user@example.com",
 *     "Special Offer!",
 *     "promo-template",
 *     Map.of("discount", "50%")
 * );
 *
 * // Send support email (automatically uses support@drimzsport.com - replyable)
 * client.sendSupportEmail(
 *     "user@example.com",
 *     "Ticket Created",
 *     "support-ticket-template",
 *     Map.of("ticketId", "12345")
 * );
 * }</pre>
 *
 * @author DrimzSport Team
 * @version 1.2.0
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

    // ==================== DEVICE TOKEN MANAGEMENT ====================

    /**
     * Registers a device token for push notifications.
     *
     * @param userId user identifier
     * @param token device token from FCM or APNS
     * @param platform platform type (ANDROID, IOS, WEB)
     * @param deviceId unique device identifier
     * @return device token response with registration details
     */
    public DeviceTokenResponse registerDevice(String userId, String token,
                                              PlatformType platform, String deviceId) {
        return registerDevice(userId, token, platform, deviceId, null);
    }

    /**
     * Registers a device token for push notifications with app version.
     *
     * @param userId user identifier
     * @param token device token from FCM or APNS
     * @param platform platform type (ANDROID, IOS, WEB)
     * @param deviceId unique device identifier
     * @param appVersion application version (optional)
     * @return device token response with registration details
     */
    public DeviceTokenResponse registerDevice(String userId, String token,
                                              PlatformType platform, String deviceId,
                                              String appVersion) {
        log.debug("Registering device token: userId={}, platform={}, deviceId={}",
                userId, platform, deviceId);

        RegisterDeviceTokenRequest request = RegisterDeviceTokenRequest.builder()
                .userId(userId)
                .token(token)
                .platform(platform)
                .deviceId(deviceId)
                .appVersion(appVersion)
                .build();

        return webClient.post()
                .uri("/api/v1/devices/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DeviceTokenResponse.class)
                .retryWhen(createRetrySpec())
                .onErrorMap(this::mapException)
                .doOnSuccess(response -> log.debug("Device registered: id={}", response.getId()))
                .block();
    }

    /**
     * Unregisters a device token.
     *
     * @param userId user identifier
     * @param deviceId device identifier to unregister
     */
    public void unregisterDevice(String userId, String deviceId) {
        log.debug("Unregistering device: userId={}, deviceId={}", userId, deviceId);

        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/devices/{deviceId}")
                        .queryParam("userId", userId)
                        .build(deviceId))
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorMap(this::mapException)
                .doOnSuccess(v -> log.debug("Device unregistered: deviceId={}", deviceId))
                .block();
    }

    /**
     * Refreshes a device token (when token changes).
     *
     * @param userId user identifier
     * @param oldToken old device token
     * @param newToken new device token
     */
    public void refreshDeviceToken(String userId, String oldToken, String newToken) {
        log.debug("Refreshing device token: userId={}", userId);

        webClient.put()
                .uri("/api/v1/devices/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "userId", userId,
                        "oldToken", oldToken,
                        "newToken", newToken
                ))
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorMap(this::mapException)
                .doOnSuccess(v -> log.debug("Device token refreshed: userId={}", userId))
                .block();
    }

    // ==================== SEND EMAIL - BASIC ====================

    /**
     * Sends a basic email notification (uses default email account).
     * For automatic routing based on email type, use specific methods like
     * sendMarketingEmail, sendSupportEmail, etc.
     *
     * @param email recipient email address
     * @param subject email subject
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendEmail(String email, String subject, String templateId,
                                          Map<String, Object> variables) {
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
                                          Map<String, Object> variables,
                                          NotificationPriority priority) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.EMAIL_VERIFICATION)
                .channel(NotificationChannel.EMAIL)
                .recipient(email)
                .subject(subject)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(priority != null ? priority : NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    // ==================== SEND EMAIL - MULTI-ACCOUNT SUPPORT ====================

    /**
     * Sends an email using a specific email account by name.
     *
     * @param emailAccountName email account name (e.g., "marketing", "support", "notifications")
     * @param recipient recipient email address
     * @param subject email subject
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendEmailFrom(String emailAccountName, String recipient,
                                              String subject, String templateId,
                                              Map<String, Object> variables) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.EMAIL_VERIFICATION) // Generic type
                .channel(NotificationChannel.EMAIL)
                .recipient(recipient)
                .subject(subject)
                .templateId(templateId)
                .templateVariables(variables)
                .emailAccountName(emailAccountName) // NEW: Specify account by name
                .priority(NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    /**
     * Sends a marketing/promotional email.
     * Automatically uses marketing email account (marketing@drimzsport.com - no-reply).
     *
     * @param recipient recipient email address
     * @param subject email subject
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendMarketingEmail(String recipient, String subject,
                                                   String templateId, Map<String, Object> variables) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.PROMOTIONAL_OFFER) // Auto-routes to marketing account
                .channel(NotificationChannel.EMAIL)
                .recipient(recipient)
                .subject(subject)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    /**
     * Sends a customer support email.
     * Automatically uses support email account (support@drimzsport.com - replyable).
     *
     * @param recipient recipient email address
     * @param subject email subject
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendSupportEmail(String recipient, String subject,
                                                 String templateId, Map<String, Object> variables) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.KYC_REVIEW_REQUIRED) // Auto-routes to support account
                .channel(NotificationChannel.EMAIL)
                .recipient(recipient)
                .subject(subject)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    /**
     * Sends a system notification email.
     * Automatically uses notifications email account (noreply@drimzsport.com - no-reply).
     *
     * @param recipient recipient email address
     * @param subject email subject
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendNotificationEmail(String recipient, String subject,
                                                      String templateId, Map<String, Object> variables) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.EMAIL_VERIFICATION) // Auto-routes to notifications account
                .channel(NotificationChannel.EMAIL)
                .recipient(recipient)
                .subject(subject)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    /**
     * Sends a welcome email to new users.
     * Automatically uses notifications email account (noreply@drimzsport.com - no-reply).
     *
     * @param recipient recipient email address
     * @param subject email subject
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendWelcomeEmail(String recipient, String subject,
                                                 String templateId, Map<String, Object> variables) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.WELCOME_EMAIL) // Auto-routes to notifications account
                .channel(NotificationChannel.EMAIL)
                .recipient(recipient)
                .subject(subject)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    /**
     * Sends a password reset email.
     * Automatically uses notifications email account (noreply@drimzsport.com - no-reply).
     *
     * @param recipient recipient email address
     * @param subject email subject
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendPasswordResetEmail(String recipient, String subject,
                                                       String templateId, Map<String, Object> variables) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.PASSWORD_RESET) // Auto-routes to notifications account
                .channel(NotificationChannel.EMAIL)
                .recipient(recipient)
                .subject(subject)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(NotificationPriority.HIGH)
                .build();

        return sendNotification(request);
    }

    /**
     * Sends a transaction receipt email.
     * Automatically uses notifications email account (noreply@drimzsport.com - no-reply).
     *
     * @param recipient recipient email address
     * @param subject email subject
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendTransactionReceiptEmail(String recipient, String subject,
                                                            String templateId, Map<String, Object> variables) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.TRANSACTION_RECEIPT) // Auto-routes to notifications account
                .channel(NotificationChannel.EMAIL)
                .recipient(recipient)
                .subject(subject)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    /**
     * Sends a KYC-related email to users.
     * Automatically uses notifications email account (noreply@drimzsport.com - no-reply).
     *
     * @param recipient recipient email address
     * @param subject email subject
     * @param kycType KYC notification type (KYC_SUBMITTED, KYC_APPROVED, KYC_REJECTED, etc.)
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendKYCEmail(String recipient, String subject,
                                             NotificationType kycType, String templateId,
                                             Map<String, Object> variables) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(kycType) // KYC_SUBMITTED, KYC_APPROVED, KYC_REJECTED, etc.
                .channel(NotificationChannel.EMAIL)
                .recipient(recipient)
                .subject(subject)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(NotificationPriority.NORMAL)
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
                                        Map<String, Object> variables) {
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
                                        Map<String, Object> variables,
                                        NotificationPriority priority) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.SMS_VERIFICATION)
                .channel(NotificationChannel.SMS)
                .recipient(phoneNumber)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(priority != null ? priority : NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    /**
     * Sends an SMS verification code.
     *
     * @param phoneNumber recipient phone number
     * @param templateId template identifier
     * @param variables template variables (should include verification code)
     * @return notification response with ID and status
     */
    public NotificationResponse sendSMSVerification(String phoneNumber, String templateId,
                                                    Map<String, Object> variables) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.SMS_VERIFICATION)
                .channel(NotificationChannel.SMS)
                .recipient(phoneNumber)
                .templateId(templateId)
                .templateVariables(variables)
                .priority(NotificationPriority.HIGH)
                .build();

        return sendNotification(request);
    }

    // ==================== SEND PUSH NOTIFICATION ====================

    /**
     * Sends a push notification to a user (all their registered devices).
     *
     * @param userId user identifier
     * @param title notification title
     * @param body notification body
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     */
    public NotificationResponse sendPushToUser(String userId, String title, String body,
                                               String templateId, Map<String, Object> variables) {
        return sendPushToUser(userId, title, body, templateId, variables, null, null, null);
    }

    /**
     * Sends a push notification to a user with custom data.
     *
     * @param userId user identifier
     * @param title notification title
     * @param body notification body
     * @param templateId template identifier
     * @param variables template variables
     * @param imageUrl optional image URL
     * @param data optional custom data
     * @param clickAction optional click action
     * @return notification response with ID and status
     */
    public NotificationResponse sendPushToUser(String userId, String title, String body,
                                               String templateId, Map<String, Object> variables,
                                               String imageUrl, Map<String, String> data,
                                               String clickAction) {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.PUSH_NOTIFICATION)
                .channel(NotificationChannel.PUSH)
                .recipient(userId) // For push, recipient is userId
                .title(title)
                .body(body)
                .templateId(templateId)
                .templateVariables(variables)
                .imageUrl(imageUrl)
                .data(data)
                .clickAction(clickAction)
                .priority(NotificationPriority.NORMAL)
                .build();

        return sendNotification(request);
    }

    /**
     * Sends a push notification directly to a specific device token.
     * Note: It's recommended to use sendPushToUser() instead, which handles all user devices.
     *
     * @param deviceToken device token
     * @param title notification title
     * @param body notification body
     * @param templateId template identifier
     * @param variables template variables
     * @return notification response with ID and status
     * @deprecated Use {@link #sendPushToUser(String, String, String, String, Map)} instead
     */
    @Deprecated(since = "1.1.0", forRemoval = false)
    public NotificationResponse sendPush(String deviceToken, String title, String body,
                                         String templateId, Map<String, Object> variables) {
        return sendPush(deviceToken, title, body, templateId, variables, null, null);
    }

    /**
     * Sends a push notification directly to a specific device token with custom data.
     * Note: It's recommended to use sendPushToUser() instead, which handles all user devices.
     *
     * @param deviceToken device token
     * @param title notification title
     * @param body notification body
     * @param templateId template identifier
     * @param variables template variables
     * @param imageUrl optional image URL
     * @param data optional custom data
     * @return notification response with ID and status
     * @deprecated Use {@link #sendPushToUser(String, String, String, String, Map, String, Map, String)} instead
     */
    @Deprecated(since = "1.1.0", forRemoval = false)
    public NotificationResponse sendPush(String deviceToken, String title, String body,
                                         String templateId, Map<String, Object> variables,
                                         String imageUrl, Map<String, String> data) {
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
                .uri("/api/v1/notifications/send")
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