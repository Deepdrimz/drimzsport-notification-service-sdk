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
 * Comprehensive examples demonstrating all features of the Notification SDK including
 * multi-email account support.
 *
 * This class contains real-world scenarios for the Drimzsport platform including:
 * - Multi-email account routing (marketing, support, notifications)
 * - Email notifications (verification, password reset, KYC, transactions)
 * - SMS notifications (verification, security alerts, match reminders)
 * - Push notifications (match updates, bet results, promotions)
 * - Bulk notifications
 * - Device management
 * - Scheduled notifications
 *
 * @author DrimzSport Team
 * @version 1.2.0
 */
public class NotificationExamples {

    private static NotificationServiceClient client;

    public static void main(String[] args) {
        // Initialize the client
        initializeClient();

        // Run examples
        System.out.println("=== MULTI-EMAIL ACCOUNT EXAMPLES ===");
        multiEmailAccountExamples();

        System.out.println("\n=== EMAIL NOTIFICATIONS (AUTOMATIC ROUTING) ===");
        welcomeEmailExample();
        passwordResetExample();
        emailVerificationExample();
        transactionReceiptExample();

        System.out.println("\n=== KYC NOTIFICATIONS ===");
        kycNotificationsForUsersExample();
        kycNotificationsForAdminsExample();

        System.out.println("\n=== MARKETING EMAILS ===");
        marketingEmailExamples();

        System.out.println("\n=== SUPPORT EMAILS ===");
        supportEmailExamples();

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

    // ==================== MULTI-EMAIL ACCOUNT EXAMPLES ====================

    /**
     * Example: Demonstrate different email account routing strategies
     */
    private static void multiEmailAccountExamples() {
        System.out.println("📧 Multi-Email Account Routing Examples");
        System.out.println("=" . repeat(50));

        // Example 1: Automatic routing based on NotificationType
        System.out.println("\n1️⃣ Automatic Routing (Recommended):");
        System.out.println("   NotificationType.PROMOTIONAL_OFFER → marketing@drimzsport.com");
        NotificationResponse auto1 = client.sendMarketingEmail(
                "user@example.com",
                "Flash Sale - 50% Off!",
                "flash-sale-template",
                Map.of("discount", "50%", "validUntil", "24 hours")
        );
        System.out.println("   ✅ Sent from marketing@ (no-reply): " + auto1.getId());

        System.out.println("\n   NotificationType.EMAIL_VERIFICATION → noreply@drimzsport.com");
        NotificationResponse auto2 = client.sendNotificationEmail(
                "newuser@example.com",
                "Verify Your Email",
                "email-verification-template",
                Map.of("code", "ABC123", "expiresIn", "24 hours")
        );
        System.out.println("   ✅ Sent from noreply@ (no-reply): " + auto2.getId());

        System.out.println("\n   NotificationType.KYC_REVIEW_REQUIRED → support@drimzsport.com");
        NotificationResponse auto3 = client.sendSupportEmail(
                "compliance@drimzsport.com",
                "KYC Review Required",
                "kyc-review-template",
                Map.of("userId", "user-123", "submissionId", "KYC-456")
        );
        System.out.println("   ✅ Sent from support@ (replyable): " + auto3.getId());

        // Example 2: Explicit email account selection
        System.out.println("\n2️⃣ Explicit Account Selection:");
        NotificationResponse explicit1 = client.sendEmailFrom(
                "marketing", // Account name
                "subscriber@example.com",
                "Monthly Newsletter",
                "newsletter-template",
                Map.of("month", "January", "year", "2026")
        );
        System.out.println("   ✅ Explicitly sent from 'marketing' account: " + explicit1.getId());

        NotificationResponse explicit2 = client.sendEmailFrom(
                "support", // Account name
                "customer@example.com",
                "Your Support Ticket #12345",
                "support-ticket-template",
                Map.of("ticketId", "12345", "status", "In Progress")
        );
        System.out.println("   ✅ Explicitly sent from 'support' account: " + explicit2.getId());

        // Example 3: Using convenience methods
        System.out.println("\n3️⃣ Convenience Methods:");
        System.out.println("   sendWelcomeEmail() → noreply@drimzsport.com");
        System.out.println("   sendMarketingEmail() → marketing@drimzsport.com");
        System.out.println("   sendSupportEmail() → support@drimzsport.com");
        System.out.println("   sendPasswordResetEmail() → noreply@drimzsport.com");

        System.out.println("\n" + "=" . repeat(50));
        System.out.println();
    }

    // ==================== EMAIL EXAMPLES - AUTOMATIC ROUTING ====================

    /**
     * Example: Send welcome email after registration
     * Automatically uses: noreply@drimzsport.com (no-reply)
     */
    private static void welcomeEmailExample() {
        System.out.println("🎉 Sending welcome email (noreply@ account)...");

        NotificationResponse response = client.sendWelcomeEmail(
                "newuser@example.com",
                "Welcome to Drimzsport!",
                "welcome-email-template",
                Map.of(
                        "userName", "John Doe",
                        "referralCode", "DRIMZ2026",
                        "bonusAmount", "50",
                        "supportEmail", "support@drimzsport.com",
                        "registrationDate", LocalDateTime.now().toString()
                )
        );

        System.out.println("✅ Welcome email sent: " + response.getId());
        System.out.println("   From: noreply@drimzsport.com (NotificationType.WELCOME_EMAIL)");
        System.out.println("   Replyable: NO");
        System.out.println();
    }

    /**
     * Example: Send password reset email
     * Automatically uses: noreply@drimzsport.com (no-reply)
     */
    private static void passwordResetExample() {
        System.out.println("🔐 Sending password reset email (noreply@ account)...");

        NotificationResponse response = client.sendPasswordResetEmail(
                "user@example.com",
                "Reset Your Password - Drimzsport",
                "password-reset-template",
                Map.of(
                        "userName", "Jane Smith",
                        "resetLink", "https://drimzsport.com/reset-password?token=xyz123abc",
                        "expiresIn", "1 hour",
                        "ipAddress", "192.168.1.100",
                        "device", "Chrome on Windows",
                        "timestamp", LocalDateTime.now().toString()
                )
        );

        System.out.println("✅ Password reset email sent: " + response.getId());
        System.out.println("   From: noreply@drimzsport.com (NotificationType.PASSWORD_RESET)");
        System.out.println("   Priority: HIGH");
        System.out.println("   Replyable: NO");
        System.out.println();
    }

    /**
     * Example: Send email verification during registration
     * Automatically uses: noreply@drimzsport.com (no-reply)
     */
    private static void emailVerificationExample() {
        System.out.println("📧 Sending email verification (noreply@ account)...");

        NotificationResponse response = client.sendNotificationEmail(
                "newuser@example.com",
                "Verify Your Email - Drimzsport",
                "email-verification-template",
                Map.of(
                        "userName", "John Doe",
                        "verificationCode", "ABC123XYZ",
                        "verificationLink", "https://drimzsport.com/verify?token=xyz",
                        "expiresIn", "24 hours"
                )
        );

        System.out.println("✅ Email verification sent: " + response.getId());
        System.out.println("   From: noreply@drimzsport.com (NotificationType.EMAIL_VERIFICATION)");
        System.out.println("   Replyable: NO");
        System.out.println();
    }

    /**
     * Example: Send transaction receipt
     * Automatically uses: noreply@drimzsport.com (no-reply)
     */
    private static void transactionReceiptExample() {
        System.out.println("💰 Sending transaction receipt (noreply@ account)...");

        NotificationResponse response = client.sendTransactionReceiptEmail(
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
                        "transactionType", "Deposit",
                        "description", "Deposit to Drimzsport wallet",
                        "balance", "1,250.00"
                )
        );

        System.out.println("✅ Transaction receipt sent: " + response.getId());
        System.out.println("   From: noreply@drimzsport.com (NotificationType.TRANSACTION_RECEIPT)");
        System.out.println("   Replyable: NO");
        System.out.println();
    }

    // ==================== KYC NOTIFICATION EXAMPLES ====================

    /**
     * Example: KYC notifications for end users
     * Automatically uses: noreply@drimzsport.com (no-reply)
     */
    private static void kycNotificationsForUsersExample() {
        System.out.println("📋 Sending KYC notifications to users (noreply@ account)...");

        // KYC Submitted
        NotificationResponse submitted = client.sendKYCEmail(
                "user@example.com",
                "KYC Documents Received",
                NotificationType.KYC_SUBMITTED,
                "kyc-submitted-template",
                Map.of(
                        "userName", "John Doe",
                        "submissionId", "KYC-001-2026",
                        "submittedDate", LocalDateTime.now().toString(),
                        "processingTime", "24-48 hours",
                        "documentsSubmitted", "Passport, Proof of Address"
                )
        );
        System.out.println("✅ KYC submitted notification: " + submitted.getId());
        System.out.println("   From: noreply@drimzsport.com");

        // KYC Approved
        NotificationResponse approved = client.sendKYCEmail(
                "user@example.com",
                "🎉 KYC Verification Approved",
                NotificationType.KYC_APPROVED,
                "kyc-approved-template",
                Map.of(
                        "userName", "John Doe",
                        "verificationId", "KYC-001-2026",
                        "approvedDate", LocalDateTime.now().toString(),
                        "accountLevel", "Verified",
                        "newLimits", "Unlimited deposits and withdrawals"
                )
        );
        System.out.println("✅ KYC approved notification: " + approved.getId());
        System.out.println("   From: noreply@drimzsport.com");

        // KYC Rejected
        NotificationResponse rejected = client.sendKYCEmail(
                "user@example.com",
                "KYC Verification - Additional Information Required",
                NotificationType.KYC_REJECTED,
                "kyc-rejected-template",
                Map.of(
                        "userName", "John Doe",
                        "verificationId", "KYC-001-2026",
                        "rejectionReason", "Document quality insufficient",
                        "requiredActions", "Please resubmit clearer photos",
                        "supportContact", "support@drimzsport.com"
                )
        );
        System.out.println("✅ KYC rejected notification: " + rejected.getId());
        System.out.println("   From: noreply@drimzsport.com");

        // KYC Resubmission Required
        NotificationResponse resubmit = client.sendKYCEmail(
                "user@example.com",
                "KYC Resubmission Required",
                NotificationType.KYC_RESUBMISSION_REQUIRED,
                "kyc-resubmission-template",
                Map.of(
                        "userName", "John Doe",
                        "reason", "Document expired",
                        "deadline", "7 days",
                        "uploadLink", "https://drimzsport.com/kyc/upload"
                )
        );
        System.out.println("✅ KYC resubmission notification: " + resubmit.getId());
        System.out.println("   From: noreply@drimzsport.com");

        System.out.println();
    }

    /**
     * Example: KYC notifications for admin/compliance team
     * Automatically uses: support@drimzsport.com (replyable)
     */
    private static void kycNotificationsForAdminsExample() {
        System.out.println("👨‍💼 Sending KYC notifications to compliance team (support@ account)...");

        // KYC Review Required
        NotificationResponse reviewRequired = client.sendSupportEmail(
                "compliance@drimzsport.com",
                "🔍 KYC Review Required - User #12345",
                "kyc-review-required-template",
                Map.of(
                        "userId", "user-12345",
                        "userName", "John Doe",
                        "submissionId", "KYC-001-2026",
                        "submittedDate", LocalDateTime.now().toString(),
                        "riskLevel", "Medium",
                        "reviewLink", "https://admin.drimzsport.com/kyc/review/KYC-001-2026"
                )
        );
        System.out.println("✅ KYC review required notification: " + reviewRequired.getId());
        System.out.println("   From: support@drimzsport.com (NotificationType.KYC_REVIEW_REQUIRED)");
        System.out.println("   Replyable: YES - Compliance team can respond");

        // Manual Verification Required
        SendNotificationRequest manualVerification = SendNotificationRequest.builder()
                .type(NotificationType.KYC_MANUAL_VERIFICATION)
                .channel(NotificationChannel.EMAIL)
                .recipient("compliance-manager@drimzsport.com")
                .subject("⚠️ Manual KYC Verification Required - High Risk User")
                .templateId("kyc-manual-verification-template")
                .templateVariables(Map.of(
                        "userId", "user-67890",
                        "userName", "Suspicious User",
                        "riskLevel", "High",
                        "flaggedReasons", "Multiple accounts, unusual activity",
                        "submissionId", "KYC-002-2026",
                        "reviewLink", "https://admin.drimzsport.com/kyc/review/KYC-002-2026"
                ))
                .priority(NotificationPriority.HIGH)
                .build();
        NotificationResponse manualVerif = client.sendNotification(manualVerification);
        System.out.println("✅ Manual verification notification: " + manualVerif.getId());
        System.out.println("   From: support@drimzsport.com (NotificationType.KYC_MANUAL_VERIFICATION)");
        System.out.println("   Priority: HIGH");
        System.out.println("   Replyable: YES");

        // SLA Breach Alert
        SendNotificationRequest slaBreach = SendNotificationRequest.builder()
                .type(NotificationType.KYC_SLA_BREACH)
                .channel(NotificationChannel.EMAIL)
                .recipient("compliance-director@drimzsport.com")
                .subject("🚨 KYC SLA BREACH ALERT - Immediate Action Required")
                .templateId("kyc-sla-breach-template")
                .templateVariables(Map.of(
                        "submissionId", "KYC-003-2026",
                        "userId", "user-99999",
                        "submittedDate", LocalDateTime.now().minusHours(50).toString(),
                        "slaTarget", "48 hours",
                        "hoursOverdue", "2",
                        "assignedTo", "John Smith",
                        "urgentActionLink", "https://admin.drimzsport.com/kyc/urgent/KYC-003-2026"
                ))
                .priority(NotificationPriority.URGENT)
                .build();
        NotificationResponse sla = client.sendNotification(slaBreach);
        System.out.println("✅ SLA breach notification: " + sla.getId());
        System.out.println("   From: support@drimzsport.com (NotificationType.KYC_SLA_BREACH)");
        System.out.println("   Priority: URGENT");
        System.out.println("   Replyable: YES");

        System.out.println();
    }

    // ==================== MARKETING EMAIL EXAMPLES ====================

    /**
     * Example: Various marketing emails
     * Automatically uses: marketing@drimzsport.com (no-reply)
     */
    private static void marketingEmailExamples() {
        System.out.println("🎯 Sending marketing emails (marketing@ account)...");

        // Flash Sale
        NotificationResponse flashSale = client.sendMarketingEmail(
                "user@example.com",
                "🔥 Flash Sale - 50% Deposit Bonus!",
                "flash-sale-template",
                Map.of(
                        "userName", "John Doe",
                        "discount", "50%",
                        "validUntil", "2 hours",
                        "minDeposit", "$20",
                        "maxBonus", "$500",
                        "promoCode", "FLASH50",
                        "ctaLink", "https://drimzsport.com/deposit?promo=FLASH50"
                )
        );
        System.out.println("✅ Flash sale email sent: " + flashSale.getId());
        System.out.println("   From: marketing@drimzsport.com (NotificationType.PROMOTIONAL_OFFER)");
        System.out.println("   Replyable: NO");

        // Match Tickets Available
        SendNotificationRequest ticketsEmail = SendNotificationRequest.builder()
                .type(NotificationType.MATCH_TICKET_AVAILABLE)
                .channel(NotificationChannel.EMAIL)
                .recipient("football-fan@example.com")
                .subject("⚽ Tickets Now Available - Real Madrid vs Barcelona")
                .templateId("match-tickets-template")
                .templateVariables(Map.of(
                        "userName", "Football Fan",
                        "homeTeam", "Real Madrid",
                        "awayTeam", "Barcelona",
                        "matchDate", "February 15, 2026",
                        "matchTime", "20:00 GMT",
                        "venue", "Santiago Bernabéu",
                        "ticketPrices", "$50 - $500",
                        "ticketsLink", "https://drimzsport.com/tickets/real-madrid-barcelona"
                ))
                .priority(NotificationPriority.NORMAL)
                .build();
        NotificationResponse tickets = client.sendNotification(ticketsEmail);
        System.out.println("✅ Match tickets email sent: " + tickets.getId());
        System.out.println("   From: marketing@drimzsport.com (NotificationType.MATCH_TICKET_AVAILABLE)");
        System.out.println("   Replyable: NO");

        // Subscription Expiring (using explicit account selection)
        NotificationResponse subscription = client.sendEmailFrom(
                "marketing",
                "premium-user@example.com",
                "Your Premium Subscription Expires in 7 Days",
                "subscription-expiring-template",
                Map.of(
                        "userName", "Premium User",
                        "subscriptionPlan", "Gold",
                        "expiryDate", "February 10, 2026",
                        "daysRemaining", "7",
                        "renewalLink", "https://drimzsport.com/subscription/renew",
                        "renewalDiscount", "20%",
                        "discountCode", "RENEW20"
                )
        );
        System.out.println("✅ Subscription expiring email sent: " + subscription.getId());
        System.out.println("   From: marketing@drimzsport.com (explicit selection)");
        System.out.println("   Replyable: NO");

        System.out.println();
    }

    // ==================== SUPPORT EMAIL EXAMPLES ====================

    /**
     * Example: Support-related emails
     * Automatically uses: support@drimzsport.com (replyable)
     */
    private static void supportEmailExamples() {
        System.out.println("🆘 Sending support emails (support@ account)...");

        // Support Ticket Created
        NotificationResponse ticketCreated = client.sendSupportEmail(
                "customer@example.com",
                "Support Ticket #12345 Created",
                "support-ticket-created-template",
                Map.of(
                        "userName", "Customer Name",
                        "ticketId", "12345",
                        "subject", "Cannot withdraw funds",
                        "priority", "High",
                        "createdDate", LocalDateTime.now().toString(),
                        "estimatedResponse", "4 hours",
                        "trackingLink", "https://drimzsport.com/support/ticket/12345"
                )
        );
        System.out.println("✅ Support ticket email sent: " + ticketCreated.getId());
        System.out.println("   From: support@drimzsport.com");
        System.out.println("   Reply-To: support@drimzsport.com");
        System.out.println("   Replyable: YES - Customer can reply directly");

        // Account Issue Resolved
        NotificationResponse issueResolved = client.sendSupportEmail(
                "customer@example.com",
                "✅ Your Issue Has Been Resolved - Ticket #12345",
                "support-issue-resolved-template",
                Map.of(
                        "userName", "Customer Name",
                        "ticketId", "12345",
                        "issueDescription", "Withdrawal processing delay",
                        "resolution", "Funds have been successfully transferred",
                        "resolvedDate", LocalDateTime.now().toString(),
                        "resolvedBy", "Support Agent Sarah",
                        "feedbackLink", "https://drimzsport.com/support/feedback/12345"
                )
        );
        System.out.println("✅ Issue resolved email sent: " + issueResolved.getId());
        System.out.println("   From: support@drimzsport.com");
        System.out.println("   Replyable: YES");

        System.out.println();
    }

    // ==================== SMS EXAMPLES ====================

    /**
     * Example: Send SMS verification code
     */
    private static void smsVerificationExample() {
        System.out.println("📱 Sending SMS verification...");

        NotificationResponse response = client.sendSMSVerification(
                "+1234567890",
                "sms-verification-template",
                Map.of(
                        "code", "123456",
                        "appName", "Drimzsport",
                        "expiresIn", "5 minutes"
                )
        );

        System.out.println("✅ SMS verification sent: " + response.getId());
        System.out.println("   Priority: HIGH");
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
                        "alertType", "New Login Detected",
                        "device", "iPhone 13 Pro",
                        "location", "New York, USA",
                        "ipAddress", "192.168.1.100",
                        "timestamp", LocalDateTime.now().toString(),
                        "actionUrl", "https://drimzsport.com/security/verify"
                ),
                NotificationPriority.URGENT
        );

        System.out.println("✅ Security alert SMS sent: " + response.getId());
        System.out.println("   Priority: URGENT");
        System.out.println();
    }

    /**
     * Example: Send transaction alert SMS
     */
    private static void smsTransactionAlertExample() {
        System.out.println("💳 Sending transaction alert SMS...");

        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.SMS_TRANSACTION_ALERT)
                .channel(NotificationChannel.SMS)
                .recipient("+1234567890")
                .templateId("sms-transaction-alert-template")
                .templateVariables(Map.of(
                        "amount", "250.00",
                        "currency", "USD",
                        "type", "Withdrawal",
                        "balance", "1,750.00",
                        "transactionId", "TXN789",
                        "timestamp", LocalDateTime.now().toString()
                ))
                .priority(NotificationPriority.NORMAL)
                .build();

        NotificationResponse response = client.sendNotification(request);
        System.out.println("✅ Transaction alert SMS sent: " + response.getId());
        System.out.println();
    }

    /**
     * Example: Send match reminder SMS
     */
    private static void smsMatchReminderExample() {
        System.out.println("⚽ Sending match reminder SMS...");

        SendNotificationRequest request = SendNotificationRequest.builder()
                .type(NotificationType.SMS_MATCH_REMINDER)
                .channel(NotificationChannel.SMS)
                .recipient("+1234567890")
                .templateId("sms-match-reminder-template")
                .templateVariables(Map.of(
                        "homeTeam", "Real Madrid",
                        "awayTeam", "Barcelona",
                        "kickoffTime", "20:00 GMT",
                        "minutesUntil", "30",
                        "venue", "Santiago Bernabéu",
                        "matchDate", "Today"
                ))
                .priority(NotificationPriority.HIGH)
                .build();

        NotificationResponse response = client.sendNotification(request);
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
                "fPzV3bX9RYi:APA91bH_sample_android_fcm_token",
                PlatformType.ANDROID,
                "android-device-001",
                "3.0.0"
        );
        System.out.println("✅ Android device registered: " + androidDevice.getId());

        // Register iOS device
        DeviceTokenResponse iosDevice = client.registerDevice(
                "user-123",
                "abc123def456_sample_apns_device_token",
                PlatformType.IOS,
                "ios-device-001",
                "3.0.0"
        );
        System.out.println("✅ iOS device registered: " + iosDevice.getId());

        // Register Web device
        DeviceTokenResponse webDevice = client.registerDevice(
                "user-123",
                "web_push_token_sample_xyz789_chrome",
                PlatformType.WEB,
                "web-device-chrome-001",
                "3.0.0"
        );
        System.out.println("✅ Web device registered: " + webDevice.getId());

        // Refresh token
        client.refreshDeviceToken(
                "user-123",
                "fPzV3bX9RYi:APA91bH_sample_android_fcm_token",
                "gQaW4cY0SZj:APA91bH_refreshed_android_fcm_token"
        );
        System.out.println("✅ Android device token refreshed");

        System.out.println();
    }

    /**
     * Example: Send match update push notifications
     */
    private static void matchUpdatePushExample() {
        System.out.println("⚽ Sending match update push notifications...");

        // Goal scored
        NotificationResponse goalNotification = client.sendPushToUser(
                "user-123",
                "⚽ GOAL! Real Madrid 2-1",
                "Benzema scores in the 78th minute!",
                "match-goal-alert-template",
                Map.of(
                        "homeTeam", "Real Madrid",
                        "awayTeam", "Barcelona",
                        "scorer", "Karim Benzema",
                        "minute", "78",
                        "currentScore", "2-1",
                        "matchId", "match-789"
                ),
                "https://cdn.drimzsport.com/images/goal-celebration.jpg",
                Map.of(
                        "matchId", "match-789",
                        "eventType", "GOAL",
                        "action", "OPEN_MATCH_DETAILS"
                ),
                "drimzsport://match/789"
        );
        System.out.println("✅ Goal notification sent: " + goalNotification.getId());

        // Match starting soon
        SendNotificationRequest matchStarting = SendNotificationRequest.builder()
                .type(NotificationType.PUSH_MATCH_UPDATE)
                .channel(NotificationChannel.PUSH)
                .recipient("user-456")
                .title("🔔 Match Starting in 10 Minutes")
                .body("Real Madrid vs Barcelona - Get ready!")
                .templateId("match-starting-template")
                .templateVariables(Map.of(
                        "homeTeam", "Real Madrid",
                        "awayTeam", "Barcelona",
                        "minutesUntilStart", "10",
                        "venue", "Santiago Bernabéu"
                ))
                .imageUrl("https://cdn.drimzsport.com/images/match-preview.jpg")
                .data(Map.of(
                        "matchId", "match-789",
                        "action", "OPEN_MATCH_DETAILS"
                ))
                .clickAction("drimzsport://match/789")
                .priority(NotificationPriority.HIGH)
                .build();
        NotificationResponse matchStart = client.sendNotification(matchStarting);
        System.out.println("✅ Match starting notification sent: " + matchStart.getId());

        // Match finished
        NotificationResponse matchFinished = client.sendPushToUser(
                "user-789",
                "⚽ Full Time: Real Madrid 3-2 Barcelona",
                "What a game! Benzema's hat-trick secures the win",
                "match-finished-template",
                Map.of(
                        "homeTeam", "Real Madrid",
                        "awayTeam", "Barcelona",
                        "finalScore", "3-2",
                        "topScorer", "Benzema (3)",
                        "highlights", "Available now"
                )
        );
        System.out.println("✅ Match finished notification sent: " + matchFinished.getId());

        System.out.println();
    }

    /**
     * Example: Send bet update push notifications
     */
    private static void betUpdatePushExample() {
        System.out.println("🎰 Sending bet update push notifications...");

        // Bet won
        NotificationResponse betWon = client.sendPushToUser(
                "user-789",
                "🎉 Congratulations! You Won!",
                "Your bet on Real Madrid won! Winnings: $250.00",
                "bet-result-won-template",
                Map.of(
                        "result", "WON",
                        "betAmount", "50.00",
                        "winnings", "250.00",
                        "currency", "USD",
                        "odds", "5.00",
                        "betType", "Match Winner",
                        "selection", "Real Madrid"
                ),
                "https://cdn.drimzsport.com/images/bet-won-celebration.jpg",
                Map.of(
                        "betId", "bet-12345",
                        "action", "OPEN_BET_DETAILS"
                ),
                "drimzsport://bet/12345"
        );
        System.out.println("✅ Bet won notification sent: " + betWon.getId());

        // Bet placed confirmation
        SendNotificationRequest betPlaced = SendNotificationRequest.builder()
                .type(NotificationType.PUSH_BET_UPDATE)
                .channel(NotificationChannel.PUSH)
                .recipient("user-789")
                .title("✅ Bet Placed Successfully")
                .body("Your $50 bet on Real Madrid to win has been confirmed")
                .templateId("bet-confirmation-template")
                .templateVariables(Map.of(
                        "betAmount", "50.00",
                        "selection", "Real Madrid to Win",
                        "odds", "2.50",
                        "potentialWin", "125.00",
                        "betId", "bet-67890",
                        "matchName", "Real Madrid vs Barcelona"
                ))
                .data(Map.of(
                        "betId", "bet-67890",
                        "action", "OPEN_BET_DETAILS"
                ))
                .clickAction("drimzsport://bet/67890")
                .priority(NotificationPriority.NORMAL)
                .build();
        NotificationResponse betPlacedResp = client.sendNotification(betPlaced);
        System.out.println("✅ Bet placed notification sent: " + betPlacedResp.getId());

        // Bet lost
        NotificationResponse betLost = client.sendPushToUser(
                "user-999",
                "😔 Bet Result",
                "Your bet on Barcelona to win was unsuccessful",
                "bet-result-lost-template",
                Map.of(
                        "result", "LOST",
                        "betAmount", "30.00",
                        "selection", "Barcelona to Win",
                        "actualResult", "Real Madrid won 3-2"
                )
        );
        System.out.println("✅ Bet lost notification sent: " + betLost.getId());

        System.out.println();
    }

    /**
     * Example: Send promotional push notification
     */
    private static void promotionalPushExample() {
        System.out.println("🎁 Sending promotional push notifications...");

        // Flash sale
        SendNotificationRequest flashSale = SendNotificationRequest.builder()
                .type(NotificationType.PUSH_PROMOTIONAL)
                .channel(NotificationChannel.PUSH)
                .recipient("user-123")
                .title("🔥 Flash Sale! 50% Deposit Bonus")
                .body("Deposit now and get 50% bonus. Valid for 2 hours only!")
                .templateId("promotional-flash-sale-template")
                .templateVariables(Map.of(
                        "discountPercentage", "50",
                        "validUntil", "2 hours",
                        "minDeposit", "20",
                        "maxBonus", "500",
                        "promoCode", "FLASH50"
                ))
                .imageUrl("https://cdn.drimzsport.com/images/flash-sale-banner.jpg")
                .data(Map.of(
                        "campaignId", "flash-sale-2026-01",
                        "promoCode", "FLASH50",
                        "action", "OPEN_DEPOSIT"
                ))
                .clickAction("drimzsport://deposit?campaign=flash-sale")
                .priority(NotificationPriority.HIGH)
                .build();
        NotificationResponse flashSaleResp = client.sendNotification(flashSale);
        System.out.println("✅ Flash sale push sent: " + flashSaleResp.getId());

        // New feature announcement
        NotificationResponse newFeature = client.sendPushToUser(
                "user-456",
                "🆕 New Feature: Live Streaming!",
                "Watch matches live while placing bets. Try it now!",
                "new-feature-announcement-template",
                Map.of(
                        "featureName", "Live Streaming",
                        "description", "Watch and bet in real-time",
                        "availableNow", "true"
                ),
                "https://cdn.drimzsport.com/images/live-streaming-promo.jpg",
                Map.of(
                        "featureId", "live-streaming",
                        "action", "EXPLORE_FEATURE"
                ),
                "drimzsport://features/live-streaming"
        );
        System.out.println("✅ New feature push sent: " + newFeature.getId());

        System.out.println();
    }

    /**
     * Example: Send system maintenance notifications
     */
    private static void systemMaintenanceExample() {
        System.out.println("🔧 Sending system maintenance notifications...");

        // Maintenance scheduled
        SendNotificationRequest scheduled = SendNotificationRequest.builder()
                .type(NotificationType.SYSTEM_MAINTENANCE_STARTED)
                .channel(NotificationChannel.PUSH)
                .recipient("user-123")
                .title("⚠️ Scheduled Maintenance Notice")
                .body("System maintenance on Jan 20, 2:00 AM - 4:00 AM GMT")
                .templateId("maintenance-scheduled-template")
                .templateVariables(Map.of(
                        "maintenanceDate", "January 20, 2026",
                        "startTime", "2:00 AM GMT",
                        "endTime", "4:00 AM GMT",
                        "duration", "2 hours",
                        "affectedServices", "Deposits and Withdrawals",
                        "message", "Please plan accordingly"
                ))
                .data(Map.of(
                        "maintenanceId", "maint-2026-01-20",
                        "action", "VIEW_DETAILS"
                ))
                .priority(NotificationPriority.HIGH)
                .build();
        NotificationResponse scheduledResp = client.sendNotification(scheduled);
        System.out.println("✅ Maintenance scheduled notification: " + scheduledResp.getId());

        // Maintenance started
        SendNotificationRequest started = SendNotificationRequest.builder()
                .type(NotificationType.SYSTEM_MAINTENANCE_STARTED)
                .channel(NotificationChannel.PUSH)
                .recipient("user-123")
                .title("🔧 Maintenance In Progress")
                .body("System maintenance has started. Some features may be unavailable.")
                .templateId("maintenance-started-template")
                .templateVariables(Map.of(
                        "startedAt", LocalDateTime.now().toString(),
                        "expectedEnd", "4:00 AM GMT",
                        "affectedServices", "Deposits, Withdrawals"
                ))
                .priority(NotificationPriority.NORMAL)
                .build();
        NotificationResponse startedResp = client.sendNotification(started);
        System.out.println("✅ Maintenance started notification: " + startedResp.getId());

        // Maintenance completed
        SendNotificationRequest completed = SendNotificationRequest.builder()
                .type(NotificationType.SYSTEM_MAINTENANCE_COMPLETED)
                .channel(NotificationChannel.PUSH)
                .recipient("user-123")
                .title("✅ Maintenance Complete")
                .body("All services are now fully operational. Thank you for your patience!")
                .templateId("maintenance-completed-template")
                .templateVariables(Map.of(
                        "completedAt", LocalDateTime.now().toString(),
                        "duration", "1 hour 45 minutes",
                        "message", "All systems operational"
                ))
                .priority(NotificationPriority.NORMAL)
                .build();
        NotificationResponse completedResp = client.sendNotification(completed);
        System.out.println("✅ Maintenance completed notification: " + completedResp.getId());

        System.out.println();
    }

    // ==================== BULK NOTIFICATION EXAMPLES ====================

    /**
     * Example: Send bulk email notifications (marketing campaign)
     */
    private static void bulkEmailExample() {
        System.out.println("📧📧📧 Sending bulk marketing emails...");

        List<SendNotificationRequest> bulkEmails = List.of(
                SendNotificationRequest.builder()
                        .type(NotificationType.PROMOTIONAL_OFFER)
                        .channel(NotificationChannel.EMAIL)
                        .recipient("user1@example.com")
                        .subject("Exclusive Weekend Offer - 50% Bonus!")
                        .templateId("weekend-promo-template")
                        .templateVariables(Map.of(
                                "userName", "Alice Johnson",
                                "offerCode", "WEEKEND50",
                                "discount", "50%",
                                "validUntil", "Sunday 11:59 PM"
                        ))
                        .priority(NotificationPriority.NORMAL)
                        .build(),

                SendNotificationRequest.builder()
                        .type(NotificationType.PROMOTIONAL_OFFER)
                        .channel(NotificationChannel.EMAIL)
                        .recipient("user2@example.com")
                        .subject("Exclusive Weekend Offer - 50% Bonus!")
                        .templateId("weekend-promo-template")
                        .templateVariables(Map.of(
                                "userName", "Bob Smith",
                                "offerCode", "WEEKEND50",
                                "discount", "50%",
                                "validUntil", "Sunday 11:59 PM"
                        ))
                        .priority(NotificationPriority.NORMAL)
                        .build(),

                SendNotificationRequest.builder()
                        .type(NotificationType.PROMOTIONAL_OFFER)
                        .channel(NotificationChannel.EMAIL)
                        .recipient("user3@example.com")
                        .subject("Exclusive Weekend Offer - 50% Bonus!")
                        .templateId("weekend-promo-template")
                        .templateVariables(Map.of(
                                "userName", "Charlie Davis",
                                "offerCode", "WEEKEND50",
                                "discount", "50%",
                                "validUntil", "Sunday 11:59 PM"
                        ))
                        .priority(NotificationPriority.NORMAL)
                        .build(),

                SendNotificationRequest.builder()
                        .type(NotificationType.MATCH_TICKET_AVAILABLE)
                        .channel(NotificationChannel.EMAIL)
                        .recipient("user4@example.com")
                        .subject("⚽ Match Tickets Available - El Clásico")
                        .templateId("match-tickets-bulk-template")
                        .templateVariables(Map.of(
                                "userName", "Diana Martinez",
                                "matchName", "Real Madrid vs Barcelona",
                                "matchDate", "February 15, 2026",
                                "ticketsLink", "https://drimzsport.com/tickets/el-clasico"
                        ))
                        .priority(NotificationPriority.NORMAL)
                        .build()
        );

        BulkNotificationResponse response = client.sendBulk(bulkEmails);
        System.out.println("✅ Bulk emails sent:");
        System.out.println("   Batch ID: " + response.getBatchId());
        System.out.println("   Total Count: " + response.getTotalCount());
        System.out.println("   Status: " + response.getStatus());
        System.out.println("   All emails sent from: marketing@drimzsport.com (no-reply)");
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
                        .title("⚽ Your Team is Playing Now!")
                        .body("Real Madrid vs Barcelona - LIVE")
                        .templateId("match-live-template")
                        .templateVariables(Map.of(
                                "team", "Real Madrid",
                                "opponent", "Barcelona",
                                "currentScore", "0-0",
                                "minute", "1"
                        ))
                        .data(Map.of("matchId", "match-001", "action", "OPEN_MATCH"))
                        .clickAction("drimzsport://match/match-001")
                        .priority(NotificationPriority.HIGH)
                        .build(),

                SendNotificationRequest.builder()
                        .type(NotificationType.PUSH_MATCH_UPDATE)
                        .channel(NotificationChannel.PUSH)
                        .recipient("user-002")
                        .title("⚽ Your Team is Playing Now!")
                        .body("Manchester United vs Liverpool - LIVE")
                        .templateId("match-live-template")
                        .templateVariables(Map.of(
                                "team", "Manchester United",
                                "opponent", "Liverpool",
                                "currentScore", "0-0",
                                "minute", "1"
                        ))
                        .data(Map.of("matchId", "match-002", "action", "OPEN_MATCH"))
                        .clickAction("drimzsport://match/match-002")
                        .priority(NotificationPriority.HIGH)
                        .build(),

                SendNotificationRequest.builder()
                        .type(NotificationType.PUSH_PROMOTIONAL)
                        .channel(NotificationChannel.PUSH)
                        .recipient("user-003")
                        .title("🎁 Special Offer Just for You!")
                        .body("Get 100% bonus on your next deposit")
                        .templateId("personalized-offer-template")
                        .templateVariables(Map.of(
                                "userName", "User Three",
                                "bonusPercent", "100",
                                "maxBonus", "200"
                        ))
                        .data(Map.of("campaignId", "personal-2026", "action", "OPEN_DEPOSIT"))
                        .priority(NotificationPriority.NORMAL)
                        .build()
        );

        BulkNotificationResponse response = client.sendBulk(bulkPushes);
        System.out.println("✅ Bulk push notifications sent:");
        System.out.println("   Batch ID: " + response.getBatchId());
        System.out.println("   Total Count: " + response.getTotalCount());
        System.out.println("   Status: " + response.getStatus());
        System.out.println();
    }

    // ==================== ADVANCED EXAMPLES ====================

    /**
     * Example: Schedule a notification for future delivery
     */
    private static void scheduledNotificationExample() {
        System.out.println("⏰ Scheduling future notifications...");

        LocalDateTime scheduledTime = LocalDateTime.now().plusHours(24);

        // Scheduled marketing email
        SendNotificationRequest scheduledPromo = SendNotificationRequest.builder()
                .type(NotificationType.PROMOTIONAL_OFFER)
                .channel(NotificationChannel.EMAIL)
                .recipient("user@example.com")
                .subject("Tomorrow's Special: 100% Deposit Bonus")
                .templateId("scheduled-promo-template")
                .templateVariables(Map.of(
                        "userName", "Valued Customer",
                        "bonusPercent", "100",
                        "validDate", "Tomorrow only",
                        "promoCode", "TOMORROW100"
                ))
                .scheduledAt(scheduledTime)
                .priority(NotificationPriority.NORMAL)
                .build();

        NotificationResponse response1 = client.sendNotification(scheduledPromo);
        System.out.println("✅ Marketing email scheduled:");
        System.out.println("   ID: " + response1.getId());
        System.out.println("   From: marketing@drimzsport.com (automatic routing)");
        System.out.println("   Scheduled for: " + response1.getScheduledAt());

        // Scheduled match reminder
        LocalDateTime matchTime = LocalDateTime.now().plusHours(2);
        SendNotificationRequest matchReminder = SendNotificationRequest.builder()
                .type(NotificationType.PUSH_MATCH_UPDATE)
                .channel(NotificationChannel.PUSH)
                .recipient("user-777")
                .title("⚽ Match Starting in 2 Hours")
                .body("Don't forget: Real Madrid vs Barcelona")
                .templateId("match-reminder-template")
                .templateVariables(Map.of(
                        "homeTeam", "Real Madrid",
                        "awayTeam", "Barcelona",
                        "kickoffTime", matchTime.toString()
                ))
                .scheduledAt(matchTime.minusMinutes(30)) // 30 minutes before match
                .priority(NotificationPriority.HIGH)
                .build();

        NotificationResponse response2 = client.sendNotification(matchReminder);
        System.out.println("✅ Match reminder push scheduled:");
        System.out.println("   ID: " + response2.getId());
        System.out.println("   Scheduled for: " + response2.getScheduledAt());

        System.out.println();
    }

    /**
     * Example: Check notification status and track delivery
     */
    private static void checkNotificationStatusExample() {
        System.out.println("🔍 Checking notification status...");

        // Send a notification
        NotificationResponse sent = client.sendNotificationEmail(
                "user@example.com",
                "Status Check Test Email",
                "test-template",
                Map.of("message", "This is a test for status checking")
        );

        System.out.println("✅ Notification sent: " + sent.getId());
        System.out.println("   Initial Status: " + sent.getStatus());

        // Wait and check status
        try {
            Thread.sleep(2000); // Wait 2 seconds

            NotificationResponse status = client.getStatus(sent.getId());
            System.out.println("\n📊 Notification Status Details:");
            System.out.println("   ID: " + status.getId());
            System.out.println("   Type: " + status.getType());
            System.out.println("   Channel: " + status.getChannel());
            System.out.println("   Recipient: " + status.getRecipient());
            System.out.println("   Status: " + status.getStatus());
            System.out.println("   Priority: " + status.getPriority());
            System.out.println("   Provider: " + (status.getProviderName() != null ? status.getProviderName() : "Not yet sent"));
            System.out.println("   Template ID: " + status.getTemplateId());
            System.out.println("   Retry Count: " + status.getRetryCount());
            System.out.println("   Created At: " + status.getCreatedAt());
            if (status.getSentAt() != null) {
                System.out.println("   Sent At: " + status.getSentAt());
            }
            if (status.getScheduledAt() != null) {
                System.out.println("   Scheduled At: " + status.getScheduledAt());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Status check interrupted");
        }

        System.out.println();
    }

    /**
     * Example: Send custom notification with full control and advanced features
     */
    private static void customNotificationExample() {
        System.out.println("🎨 Sending custom notifications with advanced features...");

        // Custom email with CC and BCC (using explicit account)
        SendNotificationRequest customEmail = SendNotificationRequest.builder()
                .type(NotificationType.SUBSCRIPTION_EXPIRING)
                .channel(NotificationChannel.EMAIL)
                .recipient("premium.user@example.com")
                .subject("⚠️ Your Premium Subscription Expires in 3 Days")
                .templateId("subscription-expiring-vip-template")
                .templateVariables(Map.of(
                        "userName", "VIP Premium User",
                        "subscriptionPlan", "Platinum",
                        "currentLevel", "Level 10",
                        "expiryDate", "February 5, 2026",
                        "daysRemaining", "3",
                        "renewalLink", "https://drimzsport.com/subscription/renew",
                        "discountCode", "VIP30",
                        "discountAmount", "30%",
                        "exclusivePerks", "Free match tickets, Priority support",
                        "lifetimeValue", "$5,000"
                ))
                .priority(NotificationPriority.HIGH)
                .scheduledAt(null) // Send immediately
                .ccRecipients(List.of("vip-support@drimzsport.com"))
                .bccRecipients(List.of("vip-analytics@drimzsport.com", "retention@drimzsport.com"))
                .build();

        NotificationResponse customResp = client.sendNotification(customEmail);
        System.out.println("✅ Custom VIP email sent: " + customResp.getId());
        System.out.println("   From: marketing@drimzsport.com (explicit selection)");
        System.out.println("   Priority: HIGH");
        System.out.println("   CC: vip-support@drimzsport.com");
        System.out.println("   BCC: 2 recipients (analytics + retention)");

        // Custom push with rich media
        SendNotificationRequest richPush = SendNotificationRequest.builder()
                .type(NotificationType.PUSH_BET_UPDATE)
                .channel(NotificationChannel.PUSH)
                .recipient("user-888")
                .title("🏆 Jackpot Alert!")
                .body("You're one match away from winning $10,000!")
                .templateId("jackpot-alert-template")
                .templateVariables(Map.of(
                        "userName", "Lucky Player",
                        "currentAmount", "10,000",
                        "matchesRemaining", "1",
                        "nextMatch", "PSG vs Bayern Munich"
                ))
                .imageUrl("https://cdn.drimzsport.com/images/jackpot-alert-big.jpg")
                .data(Map.of(
                        "betId", "jackpot-bet-999",
                        "jackpotAmount", "10000",
                        "action", "OPEN_BET_DETAILS",
                        "urgency", "high"
                ))
                .clickAction("drimzsport://bet/jackpot-bet-999")
                .priority(NotificationPriority.URGENT)
                .build();

        NotificationResponse richPushResp = client.sendNotification(richPush);
        System.out.println("✅ Rich push notification sent: " + richPushResp.getId());
        System.out.println("   Priority: URGENT");
        System.out.println("   Includes: Large image, custom data, click action");

        System.out.println();
    }
}