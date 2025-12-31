package org.deepdrimz.drimzsport.notification.sdk.examples;

import org.deepdrimz.drimzsport.notification.sdk.client.NotificationServiceClient;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationChannel;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationPriority;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.NotificationType;
import org.deepdrimz.drimzsport.notification.sdk.model.enums.PlatformType;
import org.deepdrimz.drimzsport.notification.sdk.model.request.SendNotificationRequest;
import org.deepdrimz.drimzsport.notification.sdk.model.response.BulkNotificationResponse;
import org.deepdrimz.drimzsport.notification.sdk.model.response.DeviceTokenResponse;
import org.deepdrimz.drimzsport.notification.sdk.model.response.NotificationResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Comprehensive examples demonstrating all features of the Notification SDK.
 *
 * This class contains real-world scenarios for the Drimzsport platform including:
 * - Email notifications (verification, password reset, KYC, transactions)
 * - SMS notifications (verification, security alerts, match reminders)
 * - Push notifications (match updates, bet results, promotions)
 * - Bulk notifications
 * - Device management
 * - Scheduled notifications
 *
 * @author DrimzSport Team
 * @version 1.1.0
 */
public class NotificationExamples {

    private static NotificationServiceClient client;

    public static void main(String[] args) {
        // Initialize the client
        initializeClient();

        // Run examples
        System.out.println("=== EMAIL NOTIFICATIONS ===");
        emailVerificationExample();
        passwordResetExample();
        welcomeEmailExample();
        transactionReceiptExample();
        kycNotificationsExample();

        System.out.println("\n=== SMS NOTIFICATIONS ===");
        smsVerificationExample();
        smsSecurityAlertExample();
        smsTransactionAlertExample();
        smsMatchReminderExample();

        System.out.println("\n=== PUSH NOTIFICATIONS ===");
        deviceManagementExample();
        matchUpdatePushExample();
        betUpdatePushExample();
        promotionalPushExample();
        systemMaintenanceExample();

        System.out.println("\n=== BULK NOTIFICATIONS ===");
        bulkEmailExample();
        bulkPushExample();

        System.out.println("\n=== ADVANCED FEATURES ===");
        scheduledNotificationExample();
        checkNotificationStatusExample();
        customNotificationExample();

        System.out.println("\n=== All examples completed successfully! ===");
    }

    private static void initializeClient() {
        client = NotificationServiceClient.builder()
                .baseUrl("https://api.drimzsport.com")
                .apiKey("your-api-key-here")
                .enableLogging(true)
                .maxRetries(3)
                .build();

        System.out.println("✅ Notification client initialized\n");
    }

    // ==================== EMAIL EXAMPLES ====================

    /**
     * Example: Send email verification during user registration
     */
    private static void emailVerificationExample() {
        System.out.println("📧 Sending email verification...");

        NotificationResponse response = client.sendEmail(
                "newuser@example.com",
                "Verify Your Email - Drimzsport",
                "email-verification-template",
                Map.of(
                        "userName", "John Doe",
                        "verificationCode", "ABC123",
                        "verificationLink", "https://drimzsport.com/verify?token=xyz",
                        "expiresIn", "24 hours"
                ),
                "HIGH"
        );

        System.out.println("✅ Email verification sent: " + response.getId());
        System.out.println("   Status: " + response.getStatus());
        System.out.println();
    }

    /**
     * Example: Send password reset email
     */
    private static void passwordResetExample() {
        System.out.println("🔐 Sending password reset email...");

        NotificationResponse response = client.sendEmail(
                "user@example.com",
                "Reset Your Password - Drimzsport",
                "password-reset-template",
                Map.of(
                        "userName", "Jane Smith",
                        "resetLink", "https://drimzsport.com/reset-password?token=xyz123",
                        "expiresIn", "1 hour",
                        "ipAddress", "192.168.1.100",
                        "timestamp", LocalDateTime.now().toString()
                ),
                "URGENT"
        );

        System.out.println("✅ Password reset email sent: " + response.getId());
        System.out.println();
    }

    /**
     * Example: Send welcome email after registration
     */
    private static void welcomeEmailExample() {
        System.out.println("🎉 Sending welcome email...");

        NotificationResponse response = client.sendEmail(
                "newuser@example.com",
                "Welcome to Drimzsport!",
                "welcome-email-template",
                Map.of(
                        "userName", "John Doe",
                        "referralCode", "DRIMZ2025",
                        "bonusAmount", "50",
                        "supportEmail", "support@drimzsport.com"
                )
        );

        System.out.println("✅ Welcome email sent: " + response.getId());
        System.out.println();
    }

    /**
     * Example: Send transaction receipt
     */
    private static void transactionReceiptExample() {
        System.out.println("💰 Sending transaction receipt...");

        NotificationResponse response = client.sendEmail(
                "user@example.com",
                "Payment Receipt - Transaction #TXN123456",
                "transaction-receipt-template",
                Map.of(
                        "userName", "John Doe",
                        "transactionId", "TXN123456",
                        "amount", "100.00",
                        "currency", "USD",
                        "paymentMethod", "Credit Card ****1234",
                        "transactionDate", LocalDateTime.now().toString(),
                        "description", "Deposit to Drimzsport wallet"
                )
        );

        System.out.println("✅ Transaction receipt sent: " + response.getId());
        System.out.println();
    }

    /**
     * Example: Send KYC notification series
     */
    private static void kycNotificationsExample() {
        System.out.println("📋 Sending KYC notifications...");

        // KYC Submitted
        NotificationResponse submitted = client.sendEmail(
                "user@example.com",
                "KYC Documents Received",
                "kyc-submitted-template",
                Map.of(
                        "userName", "John Doe",
                        "submissionId", "KYC-001",
                        "submittedDate", LocalDateTime.now().toString(),
                        "processingTime", "24-48 hours"
                )
        );
        System.out.println("✅ KYC submitted notification: " + submitted.getId());

        // KYC Approved
        NotificationResponse approved = client.sendEmail(
                "user@example.com",
                "KYC Verification Approved",
                "kyc-approved-template",
                Map.of(
                        "userName", "John Doe",
                        "verificationId", "KYC-001",
                        "approvedDate", LocalDateTime.now().toString(),
                        "accountLevel", "Premium"
                ),
                "HIGH"
        );
        System.out.println("✅ KYC approved notification: " + approved.getId());
        System.out.println();
    }

    // ==================== SMS EXAMPLES ====================

    /**
     * Example: Send SMS verification code
     */
    private static void smsVerificationExample() {
        System.out.println("📱 Sending SMS verification...");

        NotificationResponse response = client.sendSMS(
                "+1234567890",
                "sms-verification-template",
                Map.of(
                        "code", "123456",
                        "expiresIn", "5 minutes"
                ),
                "URGENT"
        );

        System.out.println("✅ SMS verification sent: " + response.getId());
        System.out.println();
    }

    /**
     * Example: Send security alert SMS
     */
    private static void smsSecurityAlertExample() {
        System.out.println("🔒 Sending security alert SMS...");

        NotificationResponse response = client.sendSMS(
                "+1234567890",
                "sms-security-alert-template",
                Map.of(
                        "alertType", "New Login",
                        "device", "iPhone 13",
                        "location", "New York, USA",
                        "timestamp", LocalDateTime.now().toString(),
                        "actionRequired", "If this wasn't you, secure your account immediately"
                ),
                "URGENT"
        );

        System.out.println("✅ Security alert SMS sent: " + response.getId());
        System.out.println();
    }

    /**
     * Example: Send transaction alert SMS
     */
    private static void smsTransactionAlertExample() {
        System.out.println("💳 Sending transaction alert SMS...");

        NotificationResponse response = client.sendSMS(
                "+1234567890",
                "sms-transaction-alert-template",
                Map.of(
                        "amount", "250.00",
                        "currency", "USD",
                        "type", "Withdrawal",
                        "balance", "1,750.00",
                        "timestamp", LocalDateTime.now().toString()
                )
        );

        System.out.println("✅ Transaction alert SMS sent: " + response.getId());
        System.out.println();
    }

    /**
     * Example: Send match reminder SMS
     */
    private static void smsMatchReminderExample() {
        System.out.println("⚽ Sending match reminder SMS...");

        NotificationResponse response = client.sendSMS(
                "+1234567890",
                "sms-match-reminder-template",
                Map.of(
                        "homeTeam", "Real Madrid",
                        "awayTeam", "Barcelona",
                        "kickoffTime", "20:00 GMT",
                        "venue", "Santiago Bernabéu",
                        "matchDate", "January 15, 2026"
                )
        );

        System.out.println("✅ Match reminder SMS sent: " + response.getId());
        System.out.println();
    }

    // ==================== PUSH NOTIFICATION EXAMPLES ====================

    /**
     * Example: Complete device management workflow
     */
    private static void deviceManagementExample() {
        System.out.println("📲 Managing device tokens...");

        // Register Android device
        DeviceTokenResponse androidDevice = client.registerDevice(
                "user-123",
                "fPzV3bX9RYi:APA91bH_sample_android_token",
                PlatformType.ANDROID,
                "android-device-001",
                "2.5.0"
        );
        System.out.println("✅ Android device registered: " + androidDevice.getId());

        // Register iOS device
        DeviceTokenResponse iosDevice = client.registerDevice(
                "user-123",
                "abc123def456_sample_ios_token",
                PlatformType.IOS,
                "ios-device-001",
                "2.5.0"
        );
        System.out.println("✅ iOS device registered: " + iosDevice.getId());

        // Register Web device
        DeviceTokenResponse webDevice = client.registerDevice(
                "user-123",
                "web_token_sample_xyz789",
                PlatformType.WEB,
                "web-device-001",
                "2.5.0"
        );
        System.out.println("✅ Web device registered: " + webDevice.getId());

        // Refresh token (simulating token refresh on mobile)
        client.refreshDeviceToken(
                "user-123",
                "fPzV3bX9RYi:APA91bH_sample_android_token",
                "gQaW4cY0SZj:APA91bH_new_android_token"
        );
        System.out.println("✅ Android device token refreshed");

        System.out.println();
    }

    /**
     * Example: Send match update push notifications
     */
    private static void matchUpdatePushExample() {
        System.out.println("⚽ Sending match update push...");

        // Goal scored notification
        NotificationResponse goalNotification = client.sendPushToUser(
                "user-123",
                "⚽ GOAL! Real Madrid 2-1",
                "Benzema scores in the 78th minute!",
                "match-goal-alert-template",
                Map.of(
                        "homeTeam", "Real Madrid",
                        "awayTeam", "Barcelona",
                        "scorer", "Benzema",
                        "minute", "78",
                        "currentScore", "2-1"
                ),
                "https://cdn.drimzsport.com/goal-celebration.jpg",
                Map.of(
                        "matchId", "match-789",
                        "eventType", "GOAL",
                        "action", "OPEN_MATCH_DETAILS"
                ),
                "drimzsport://match/789"
        );
        System.out.println("✅ Goal notification sent: " + goalNotification.getId());

        // Match starting soon
        NotificationResponse matchStarting = client.sendPushToUser(
                "user-456",
                "🔔 Match Starting Soon",
                "Real Madrid vs Barcelona starts in 10 minutes",
                "match-starting-template",
                Map.of(
                        "homeTeam", "Real Madrid",
                        "awayTeam", "Barcelona",
                        "minutesUntilStart", "10"
                ),
                "https://cdn.drimzsport.com/match-preview.jpg",
                Map.of(
                        "matchId", "match-789",
                        "action", "OPEN_MATCH_DETAILS"
                ),
                "drimzsport://match/789"
        );
        System.out.println("✅ Match starting notification sent: " + matchStarting.getId());

        System.out.println();
    }

    /**
     * Example: Send bet update push notifications
     */
    private static void betUpdatePushExample() {
        System.out.println("🎰 Sending bet update push...");

        // Bet won notification
        NotificationResponse betWon = client.sendPushToUser(
                "user-789",
                "🎉 Congratulations! You Won!",
                "Your bet on Real Madrid won! Winnings: $250.00",
                "bet-result-template",
                Map.of(
                        "result", "WON",
                        "betAmount", "50.00",
                        "winnings", "250.00",
                        "currency", "USD",
                        "odds", "5.00"
                ),
                null,
                Map.of(
                        "betId", "bet-12345",
                        "action", "OPEN_BET_DETAILS"
                ),
                "drimzsport://bet/12345"
        );
        System.out.println("✅ Bet won notification sent: " + betWon.getId());

        // Bet placed confirmation
        NotificationResponse betPlaced = client.sendPushToUser(
                "user-789",
                "✅ Bet Placed Successfully",
                "Your $50 bet on Real Madrid to win has been confirmed",
                "bet-confirmation-template",
                Map.of(
                        "betAmount", "50.00",
                        "selection", "Real Madrid to Win",
                        "odds", "2.50",
                        "potentialWin", "125.00"
                )
        );
        System.out.println("✅ Bet placed notification sent: " + betPlaced.getId());

        System.out.println();
    }

    /**
     * Example: Send promotional push notification
     */
    private static void promotionalPushExample() {
        System.out.println("🎁 Sending promotional push...");

        NotificationResponse promotion = client.sendPushToUser(
                "user-123",
                "🔥 Flash Sale! 50% Bonus",
                "Deposit now and get 50% bonus. Valid for 2 hours only!",
                "promotional-flash-sale-template",
                Map.of(
                        "discountPercentage", "50",
                        "validUntil", "2 hours",
                        "minDeposit", "20",
                        "maxBonus", "500"
                ),
                "https://cdn.drimzsport.com/flash-sale-banner.jpg",
                Map.of(
                        "campaignId", "flash-sale-2026-01",
                        "action", "OPEN_DEPOSIT"
                ),
                "drimzsport://deposit?campaign=flash-sale"
        );

        System.out.println("✅ Promotional push sent: " + promotion.getId());
        System.out.println();
    }

    /**
     * Example: Send system maintenance notification
     */
    private static void systemMaintenanceExample() {
        System.out.println("🔧 Sending system maintenance push...");

        // Maintenance scheduled
        NotificationResponse scheduled = client.sendPushToUser(
                "user-123",
                "⚠️ Scheduled Maintenance",
                "System maintenance on Jan 20, 2:00 AM - 4:00 AM GMT",
                "maintenance-scheduled-template",
                Map.of(
                        "maintenanceDate", "January 20, 2026",
                        "startTime", "2:00 AM GMT",
                        "endTime", "4:00 AM GMT",
                        "duration", "2 hours",
                        "affectedServices", "Deposits, Withdrawals"
                )
        );
        System.out.println("✅ Maintenance scheduled notification: " + scheduled.getId());

        // Maintenance completed
        NotificationResponse completed = client.sendPushToUser(
                "user-123",
                "✅ Maintenance Complete",
                "All services are now fully operational",
                "maintenance-completed-template",
                Map.of(
                        "completedAt", LocalDateTime.now().toString(),
                        "message", "Thank you for your patience"
                )
        );
        System.out.println("✅ Maintenance completed notification: " + completed.getId());

        System.out.println();
    }

    // ==================== BULK NOTIFICATION EXAMPLES ====================

    /**
     * Example: Send bulk email notifications
     */
    private static void bulkEmailExample() {
        System.out.println("📧📧📧 Sending bulk emails...");

        List<SendNotificationRequest> bulkEmails = List.of(
                SendNotificationRequest.builder()
                        .type(NotificationType.PROMOTIONAL_OFFER)
                        .channel(NotificationChannel.EMAIL)
                        .recipient("user1@example.com")
                        .subject("Exclusive Weekend Offer!")
                        .templateId("weekend-promo-template")
                        .templateVariables(Map.of(
                                "userName", "User One",
                                "offerCode", "WEEKEND50",
                                "discount", "50%"
                        ))
                        .priority(NotificationPriority.NORMAL)
                        .build(),

                SendNotificationRequest.builder()
                        .type(NotificationType.PROMOTIONAL_OFFER)
                        .channel(NotificationChannel.EMAIL)
                        .recipient("user2@example.com")
                        .subject("Exclusive Weekend Offer!")
                        .templateId("weekend-promo-template")
                        .templateVariables(Map.of(
                                "userName", "User Two",
                                "offerCode", "WEEKEND50",
                                "discount", "50%"
                        ))
                        .priority(NotificationPriority.NORMAL)
                        .build(),

                SendNotificationRequest.builder()
                        .type(NotificationType.PROMOTIONAL_OFFER)
                        .channel(NotificationChannel.EMAIL)
                        .recipient("user3@example.com")
                        .subject("Exclusive Weekend Offer!")
                        .templateId("weekend-promo-template")
                        .templateVariables(Map.of(
                                "userName", "User Three",
                                "offerCode", "WEEKEND50",
                                "discount", "50%"
                        ))
                        .priority(NotificationPriority.NORMAL)
                        .build()
        );

        BulkNotificationResponse response = client.sendBulk(bulkEmails);
        System.out.println("✅ Bulk emails sent:");
        System.out.println("   Batch ID: " + response.getBatchId());
        System.out.println("   Total Count: " + response.getTotalCount());
        System.out.println("   Status: " + response.getStatus());
        System.out.println();
    }

    /**
     * Example: Send bulk push notifications
     */
    private static void bulkPushExample() {
        System.out.println("📲📲📲 Sending bulk push notifications...");

        List<SendNotificationRequest> bulkPushes = List.of(
                SendNotificationRequest.builder()
                        .type(NotificationType.PUSH_MATCH_UPDATE)
                        .channel(NotificationChannel.PUSH)
                        .recipient("user-001")
                        .title("⚽ Match Alert")
                        .body("Your favorite team is playing now!")
                        .templateId("match-live-template")
                        .templateVariables(Map.of(
                                "team", "Real Madrid",
                                "opponent", "Barcelona"
                        ))
                        .data(Map.of("matchId", "match-001", "action", "OPEN_MATCH"))
                        .priority(NotificationPriority.HIGH)
                        .build(),

                SendNotificationRequest.builder()
                        .type(NotificationType.PUSH_MATCH_UPDATE)
                        .channel(NotificationChannel.PUSH)
                        .recipient("user-002")
                        .title("⚽ Match Alert")
                        .body("Your favorite team is playing now!")
                        .templateId("match-live-template")
                        .templateVariables(Map.of(
                                "team", "Manchester United",
                                "opponent", "Liverpool"
                        ))
                        .data(Map.of("matchId", "match-002", "action", "OPEN_MATCH"))
                        .priority(NotificationPriority.HIGH)
                        .build()
        );

        BulkNotificationResponse response = client.sendBulk(bulkPushes);
        System.out.println("✅ Bulk push notifications sent:");
        System.out.println("   Batch ID: " + response.getBatchId());
        System.out.println("   Total Count: " + response.getTotalCount());
        System.out.println();
    }

    // ==================== ADVANCED EXAMPLES ====================

    /**
     * Example: Schedule a notification for future delivery
     */
    private static void scheduledNotificationExample() {
        System.out.println("⏰ Scheduling notification...");

        LocalDateTime scheduledTime = LocalDateTime.now().plusHours(24);

        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.MATCH_TICKET_AVAILABLE)
                .channel(NotificationChannel.EMAIL)
                .recipient("user@example.com")
                .subject("Match Tickets Available - Real Madrid vs Barcelona")
                .templateId("ticket-available-template")
                .templateVariables(Map.of(
                        "matchName", "Real Madrid vs Barcelona",
                        "matchDate", "January 25, 2026",
                        "ticketLink", "https://drimzsport.com/tickets/match-789"
                ))
                .scheduledAt(scheduledTime)
                .priority(NotificationPriority.NORMAL)
                .build();

        NotificationResponse response = client.sendNotification(request);
        System.out.println("✅ Notification scheduled:");
        System.out.println("   ID: " + response.getId());
        System.out.println("   Scheduled for: " + response.getScheduledAt());
        System.out.println();
    }

    /**
     * Example: Check notification status
     */
    private static void checkNotificationStatusExample() {
        System.out.println("🔍 Checking notification status...");

        // First, send a notification
        NotificationResponse sent = client.sendEmail(
                "user@example.com",
                "Test Email",
                "test-template",
                Map.of("message", "This is a test")
        );

        System.out.println("✅ Notification sent: " + sent.getId());

        // Check its status
        try {
            Thread.sleep(2000); // Wait 2 seconds

            NotificationResponse status = client.getStatus(sent.getId());
            System.out.println("📊 Notification Status:");
            System.out.println("   ID: " + status.getId());
            System.out.println("   Type: " + status.getType());
            System.out.println("   Channel: " + status.getChannel());
            System.out.println("   Status: " + status.getStatus());
            System.out.println("   Provider: " + status.getProviderName());
            System.out.println("   Retry Count: " + status.getRetryCount());
            if (status.getSentAt() != null) {
                System.out.println("   Sent At: " + status.getSentAt());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println();
    }

    /**
     * Example: Send custom notification with full control
     */
    private static void customNotificationExample() {
        System.out.println("🎨 Sending custom notification...");

        SendNotificationRequest customRequest = SendNotificationRequest.builder()
                .type(NotificationType.SUBSCRIPTION_EXPIRING)
                .channel(NotificationChannel.EMAIL)
                .recipient("premium.user@example.com")
                .subject("Your Premium Subscription Expires Soon")
                .templateId("subscription-expiring-template")
                .templateVariables(Map.of(
                        "userName", "Premium User",
                        "subscriptionPlan", "Gold",
                        "expiryDate", "February 1, 2026",
                        "daysRemaining", "7",
                        "renewalLink", "https://drimzsport.com/renew",
                        "discountCode", "RENEW20",
                        "discountAmount", "20%"
                ))
                .priority(NotificationPriority.HIGH)
                .scheduledAt(null) // Send immediately
                // Email-specific: Add CC and BCC
                .ccRecipients(List.of("support@drimzsport.com"))
                .bccRecipients(List.of("analytics@drimzsport.com"))
                .build();

        NotificationResponse response = client.sendNotification(customRequest);
        System.out.println("✅ Custom notification sent: " + response.getId());
        System.out.println("   Priority: " + response.getPriority());
        System.out.println("   Template: " + response.getTemplateId());
        System.out.println();
    }
}
