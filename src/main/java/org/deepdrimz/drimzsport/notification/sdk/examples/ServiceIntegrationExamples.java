package org.deepdrimz.drimzsport.notification.sdk.examples;

import org.deepdrimz.drimzsport.notification.sdk.client.NotificationServiceClient;
import org.deepdrimz.drimzsport.notification.sdk.model.response.NotificationResponse;

import java.util.Map;

/**
 * Examples showing how to integrate the SDK into Drimzsport microservices.
 *
 * These examples demonstrate typical integration patterns for:
 * - Betting Service
 * - Match Service
 * - User Service
 * - Payment Service
 * - KYC Service
 */
public class ServiceIntegrationExamples {

    // ==================== BETTING SERVICE ====================

    public static class BettingServiceIntegration {

        private final NotificationServiceClient notificationClient;

        public BettingServiceIntegration(NotificationServiceClient client) {
            this.notificationClient = client;
        }

        public void onBetPlaced(String userId, String betId, double amount, String selection, double odds) {

            notificationClient.sendPushToUser(
                    userId,
                    "✅ Bet Placed Successfully",
                    String.format("Your $%.2f bet on %s has been confirmed", amount, selection),
                    "bet-confirmation-template",
                    Map.of(
                            "betAmount", amount,
                            "selection", selection,
                            "odds", odds,
                            "potentialWin", amount * odds
                    ),
                    null,
                    Map.of(
                            "betId", betId,
                            "action", "OPEN_BET_DETAILS"
                    ),
                    "drimzsport://bet/" + betId
            );

            notificationClient.sendEmail(
                    getUserEmail(userId),
                    "Bet Confirmation - " + selection,
                    "bet-confirmation-email-template",
                    Map.of(
                            "userName", getUserName(userId),
                            "betId", betId,
                            "betAmount", amount,
                            "selection", selection,
                            "odds", odds
                    )
            );
        }

        public void onBetWon(String userId, String betId, double betAmount, double winnings) {

            notificationClient.sendPushToUser(
                    userId,
                    "🎉 Congratulations! You Won!",
                    String.format("Your bet won! Winnings: $%.2f", winnings),
                    "bet-won-template",
                    Map.of(
                            "result", "WON",
                            "betAmount", betAmount,
                            "winnings", winnings,
                            "profit", winnings - betAmount
                    ),
                    null,
                    Map.of(
                            "betId", betId,
                            "action", "OPEN_BET_DETAILS"
                    ),
                    "drimzsport://bet/" + betId
            );
        }

        public void onBetLost(String userId, String betId, double betAmount) {

            notificationClient.sendPushToUser(
                    userId,
                    "😔 Bet Not Won",
                    "Better luck next time!",
                    "bet-lost-template",
                    Map.of(
                            "result", "LOST",
                            "betAmount", betAmount,
                            "encouragement", "Check today's top odds!"
                    ),
                    null,
                    Map.of(
                            "betId", betId,
                            "action", "OPEN_BET_DETAILS"
                    ),
                    "drimzsport://bet/" + betId
            );
        }
    }

    // ==================== MATCH SERVICE ====================

    public static class MatchServiceIntegration {

        private final NotificationServiceClient notificationClient;

        public MatchServiceIntegration(NotificationServiceClient client) {
            this.notificationClient = client;
        }

        public void onGoalScored(String userId, String matchId, String teamName) {

            notificationClient.sendPushToUser(
                    userId,
                    "⚽ Goal Alert!",
                    teamName + " just scored!",
                    "goal-alert-template",
                    Map.of(
                            "team", teamName,
                            "matchId", matchId
                    ),
                    null,
                    Map.of(
                            "matchId", matchId,
                            "action", "OPEN_MATCH"
                    ),
                    "drimzsport://match/" + matchId
            );
        }
    }

    // ==================== USER SERVICE ====================

    public static class UserServiceIntegration {

        private final NotificationServiceClient notificationClient;

        public UserServiceIntegration(NotificationServiceClient client) {
            this.notificationClient = client;
        }

        public NotificationResponse sendWelcomeEmail(String userId) {

            return notificationClient.sendEmail(
                    getUserEmail(userId),
                    "Welcome to DrimzSport 🎉",
                    "welcome-email-template",
                    Map.of(
                            "userName", getUserName(userId),
                            "loginUrl", "https://app.drimzsport.com/login"
                    )
            );
        }

        public void notifyPasswordReset(String userId, String resetLink) {

            notificationClient.sendEmail(
                    getUserEmail(userId),
                    "Reset Your Password",
                    "password-reset-template",
                    Map.of(
                            "userName", getUserName(userId),
                            "resetLink", resetLink
                    )
            );
        }
    }

    // ==================== PAYMENT SERVICE ====================

    public static class PaymentServiceIntegration {

        private final NotificationServiceClient notificationClient;

        public PaymentServiceIntegration(NotificationServiceClient client) {
            this.notificationClient = client;
        }

        public void onDepositSuccess(String userId, double amount) {

            notificationClient.sendPushToUser(
                    userId,
                    "💰 Deposit Successful",
                    String.format("$%.2f has been added to your wallet", amount),
                    "deposit-success-template",
                    Map.of(
                            "amount", amount
                    )
            );
        }

        public void onWithdrawalProcessed(String userId, double amount) {

            notificationClient.sendEmail(
                    getUserEmail(userId),
                    "Withdrawal Processed",
                    "withdrawal-template",
                    Map.of(
                            "userName", getUserName(userId),
                            "amount", amount
                    )
            );
        }
    }

    // ==================== KYC SERVICE ====================

    public static class KycServiceIntegration {

        private final NotificationServiceClient notificationClient;

        public KycServiceIntegration(NotificationServiceClient client) {
            this.notificationClient = client;
        }

        public void onKycApproved(String userId) {

            notificationClient.sendPushToUser(
                    userId,
                    "✅ KYC Approved",
                    "Your account is now fully verified.",
                    "kyc-approved-template",
                    Map.of(
                            "status", "APPROVED"
                    )
            );
        }

        public void onKycRejected(String userId, String reason) {

            notificationClient.sendEmail(
                    getUserEmail(userId),
                    "KYC Verification Failed",
                    "kyc-rejected-template",
                    Map.of(
                            "userName", getUserName(userId),
                            "reason", reason
                    )
            );
        }
    }

    // ==================== MOCKED HELPERS ====================

    private static String getUserEmail(String userId) {
        return userId + "@example.com";
    }

    private static String getUserName(String userId) {
        return "User-" + userId;
    }
}
