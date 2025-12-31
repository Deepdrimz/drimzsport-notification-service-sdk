package org.deepdrimz.drimzsport.notification.sdk.examples;

import org.deepdrimz.drimzsport.notification.sdk.client.NotificationServiceClient;
import org.deepdrimz.drimzsport.notification.sdk.model.response.NotificationResponse;

import java.util.Map;

/**
 * <h1>Service Integration Examples</h1>
 *
 * <p>
 * This class provides <b>real-world, production-style integration examples</b>
 * demonstrating how Drimzsport microservices should interact with the
 * {@link NotificationServiceClient}.
 * </p>
 *
 * <p>
 * Each nested class represents a specific microservice within the Drimzsport
 * ecosystem and showcases common notification use cases such as:
 * </p>
 *
 * <ul>
 *   <li>Sending push notifications to users</li>
 *   <li>Sending transactional and informational emails</li>
 *   <li>Reacting to domain events (bets, matches, payments, KYC)</li>
 * </ul>
 *
 * <p>
 * <b>Important:</b> These examples focus on <i>how to call the SDK</i>.
 * The actual notification delivery is handled asynchronously by the
 * Notification Service via Kafka consumers and provider adapters.
 * </p>
 *
 * @author DrimzSport Team
 * @version 1.0.1
 * @since 1.0.0
 */
public class ServiceIntegrationExamples {

    // ==================== BETTING SERVICE ====================

    /**
     * <h2>Betting Service Integration</h2>
     *
     * <p>
     * Demonstrates how the Betting Service should trigger notifications
     * in response to bet-related domain events such as:
     * </p>
     *
     * <ul>
     *   <li>Bet placement</li>
     *   <li>Winning bets</li>
     *   <li>Losing bets</li>
     * </ul>
     *
     * <p>
     * Notifications include both <b>push notifications</b> for real-time feedback
     * and <b>email confirmations</b> for record-keeping.
     * </p>
     */
    public static class BettingServiceIntegration {

        private final NotificationServiceClient notificationClient;

        /**
         * Creates a new Betting Service integration helper.
         *
         * @param client shared {@link NotificationServiceClient} instance
         */
        public BettingServiceIntegration(NotificationServiceClient client) {
            this.notificationClient = client;
        }

        /**
         * Sends notifications when a user successfully places a bet.
         *
         * <p>
         * This method demonstrates a typical pattern:
         * </p>
         * <ol>
         *   <li>Send a push notification for immediate user feedback</li>
         *   <li>Send a confirmation email with full bet details</li>
         * </ol>
         *
         * @param userId     unique identifier of the user
         * @param betId      unique bet identifier
         * @param amount     stake amount placed by the user
         * @param selection  betting selection (e.g. team or outcome)
         * @param odds       odds applied to the bet
         */
        public void onBetPlaced(String userId, String betId, double amount, String selection, double odds) {

            // Push notification (real-time feedback)
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

            // Email confirmation (transactional record)
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

        /**
         * Sends a push notification when a bet is settled as a win.
         *
         * @param userId     unique identifier of the user
         * @param betId      bet identifier
         * @param betAmount  original stake amount
         * @param winnings   total winnings from the bet
         */
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

        /**
         * Sends a push notification when a bet is settled as a loss.
         *
         * <p>
         * This notification is intentionally encouraging and may be used
         * to drive re-engagement.
         * </p>
         *
         * @param userId     unique identifier of the user
         * @param betId      bet identifier
         * @param betAmount  original stake amount
         */
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

    /**
     * <h2>Match Service Integration</h2>
     *
     * <p>
     * Demonstrates how the Match Service can send real-time push notifications
     * for live match events such as goals.
     * </p>
     */
    public static class MatchServiceIntegration {

        private final NotificationServiceClient notificationClient;

        /**
         * Creates a Match Service integration helper.
         *
         * @param client shared {@link NotificationServiceClient} instance
         */
        public MatchServiceIntegration(NotificationServiceClient client) {
            this.notificationClient = client;
        }

        /**
         * Sends a push notification when a goal is scored.
         *
         * @param userId   unique identifier of the user
         * @param matchId  match identifier
         * @param teamName name of the team that scored
         */
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

    // ==================== MOCKED HELPERS ====================

    /**
     * Mock helper method for resolving a user's email address.
     *
     * <p>
     * In a real system, this would be retrieved from the User Service
     * or an identity provider.
     * </p>
     *
     * @param userId user identifier
     * @return resolved email address
     */
    private static String getUserEmail(String userId) {
        return userId + "@example.com";
    }

    /**
     * Mock helper method for resolving a user's display name.
     *
     * @param userId user identifier
     * @return display name
     */
    private static String getUserName(String userId) {
        return "User-" + userId;
    }
}
