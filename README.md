# Drimzsport Notification Service SDK

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Maven Central](https://img.shields.io/badge/Maven%20Central-1.0.0-blue?style=for-the-badge)](https://search.maven.org/)
[![License](https://img.shields.io/badge/License-Proprietary-red?style=for-the-badge)](LICENSE)
[![Documentation](https://img.shields.io/badge/Docs-JavaDoc-brightgreen?style=for-the-badge)](https://docs.drimzsport.com/notification-sdk)

Java client library for the DrimzSport Notification Service. Send Email, SMS, and Push notifications with a simple, type-safe API.

## 📋 Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [Usage Examples](#usage-examples)
- [Error Handling](#error-handling)
- [Best Practices](#best-practices)
- [Migration Guide](#migration-guide)

## ✨ Features

### Core Capabilities
- **Type-Safe API**: Strongly typed requests and responses
- **Simple Methods**: 5 essential operations that cover 99% of use cases
- **Auto-Retry**: Built-in retry with exponential backoff
- **Thread-Safe**: Use across multiple threads safely
- **Spring Integration**: Auto-configuration for Spring Boot
- **Standalone**: Works without Spring framework

### Simplified for Clients
- ✅ Send Email
- ✅ Send SMS
- ✅ Send Push (iOS/Android/Web)
- ✅ Send Bulk
- ✅ Check Status

**Admin operations** (templates, metrics) are handled via direct API calls.

## 📦 Installation

### Maven

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>org.deepdrimz.drimzodds</groupId>
    <artifactId>drimzsport-notification-service-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

```

## 🚀 Quick Start (60 Seconds)

### 1. Get API Key

Contact your administrator or create via API:
```bash
curl -X POST https://api.drimzsport.com/api/v1/admin/api-keys \
  -H "Content-Type: application/json" \
  -d '{"name": "My App", "clientId": "my-app", "permissions": ["notifications:send"]}'
```

### 2. Create Client

```java
import org.deepdrimz.drimzodds.notification.sdk.client.NotificationServiceClient;

NotificationServiceClient client = NotificationServiceClient.builder()
    .baseUrl("https://api.drimzsport.com")
    .apiKey("ntf_your_api_key_here")
    .build();
```

### 3. Send Notification

```java
// Send email
client.sendEmail(
    "user@example.com",
    "Welcome to DrimzSport!",
    "welcome-template",
    Map.of("userName", "John Doe")
);

// Done! Notification sent ✅
```

## ⚙️ Configuration

### Basic Configuration

```java
NotificationServiceClient client = NotificationServiceClient.builder()
    .baseUrl("https://api.drimzsport.com")
    .apiKey("ntf_your_api_key_here")
    .build();
```

### Advanced Configuration

```java
import java.time.Duration;

NotificationServiceClient client = NotificationServiceClient.builder()
    .baseUrl("https://api.drimzsport.com")
    .apiKey("ntf_your_api_key_here")
    .connectionTimeout(Duration.ofSeconds(5))
    .readTimeout(Duration.ofSeconds(30))
    .maxRetries(3)
    .enableLogging(true)  // Enable request/response logging
    .build();
```

### Spring Boot Auto-Configuration

Add to `application.yml`:

```yaml
notification:
  sdk:
    base-url: https://api.drimzsport.com
    api-key: ${NOTIFICATION_API_KEY}
    connection-timeout: 5000
    read-timeout: 30000
    max-retries: 3
```

Then inject the client:

```java
@Service
public class NotificationService {

    @Autowired
    private NotificationServiceClient notificationClient;

    public void sendWelcomeEmail(User user) {
        notificationClient.sendEmail(
                user.getEmail(),
                "Welcome!",
                "welcome-template",
                Map.of("userName", user.getName())
        );
    }
}
```

## 📚 Usage Examples

### Bulk Notifications

```java
import org.deepdrimz.drimzodds.notification.sdk.model.request.BulkNotificationRequest;
import org.deepdrimz.drimzodds.notification.sdk.model.response.BulkNotificationResponse;

List<SendNotificationRequest> notifications = Arrays.asList(
        SendNotificationRequest.builder()
                .type(NotificationType.PROMOTIONAL_OFFER)
                .channel(NotificationChannel.EMAIL)
                .recipient("user1@example.com")
                .subject("Special Offer!")
                .templateId("promo-template")
                .templateVariables(Map.of("offerCode", "SAVE20"))
                .build(),
        SendNotificationRequest.builder()
                .type(NotificationType.PROMOTIONAL_OFFER)
                .channel(NotificationChannel.EMAIL)
                .recipient("user2@example.com")
                .subject("Special Offer!")
                .templateId("promo-template")
                .templateVariables(Map.of("offerCode", "SAVE20"))
                .build()
);

BulkNotificationRequest bulkRequest = BulkNotificationRequest.builder()
        .notifications(notifications)
        .build();

BulkNotificationResponse response = client.sendBulkNotifications(bulkRequest);
System.out.println("Batch ID: " + response.getBatchId());
        System.out.println("Total Count: " + response.getTotalCount());
```

### Query Notification Status

```java
// Get notification by ID
NotificationResponse notification = client.getNotification("notification-id");
System.out.println("Status: " + notification.getStatus());
        System.out.println("Sent At: " + notification.getSentAt());

// Get notifications by recipient
Page<NotificationResponse> notifications = client.getNotificationsByRecipient(
        "user@example.com",
        0,  // page
        20  // size
);

// Get notifications by status
Page<NotificationResponse> failedNotifications = client.getNotificationsByStatus(
        "FAILED",
        0,
        20
);
```

### Retry Failed Notification

```java
NotificationResponse retried = client.retryNotification("notification-id");
System.out.println("Retry Count: " + retried.getRetryCount());
```

### Template Management

```java
import org.deepdrimz.drimzodds.notification.sdk.model.request.CreateTemplateRequest;
import org.deepdrimz.drimzodds.notification.sdk.model.response.TemplateResponse;

// Create template
CreateTemplateRequest templateRequest = CreateTemplateRequest.builder()
        .type(NotificationType.EMAIL_VERIFICATION)
        .channel(NotificationChannel.EMAIL)
        .name("Email Verification Template")
        .subject("Verify Your Email - {{appName}}")
        .content("<html><body><h1>Hi {{userName}}!</h1><p>Code: {{code}}</p></body></html>")
        .language("en")
        .requiredVariables(Arrays.asList("userName", "code", "appName"))
        .active(true)
        .build();

TemplateResponse template = client.createTemplate(templateRequest);
System.out.println("Template ID: " + template.getId());

// Get template
TemplateResponse retrieved = client.getTemplate(template.getId());

// Delete template
client.deleteTemplate(template.getId());
```

### Metrics and Health

```java
import org.deepdrimz.drimzodds.notification.sdk.model.response.MetricsResponse;
import org.deepdrimz.drimzodds.notification.sdk.model.response.ProviderHealthResponse;

// Get current metrics
MetricsResponse metrics = client.getMetrics();
System.out.println("Total Notifications: " + metrics.getTotalNotifications());
        System.out.println("Success Rate: " + metrics.getSuccessRate() + "%");

// Get metrics by date range
LocalDateTime from = LocalDateTime.now().minusDays(7);
LocalDateTime to = LocalDateTime.now();
MetricsResponse weekMetrics = client.getMetricsByRange(from, to);

// Get provider health
List<ProviderHealthResponse> providers = client.getProviderHealthStatus();
providers.forEach(provider ->
        System.out.println(provider.getName() + ": " + provider.getStatus())
        );
```

### Scheduled Notifications

```java
SendNotificationRequest scheduledRequest = SendNotificationRequest.builder()
        .type(NotificationType.MATCH_TICKET_AVAILABLE)
        .channel(NotificationChannel.EMAIL)
        .recipient("user@example.com")
        .subject("Match Tickets Available")
        .templateId("ticket-template")
        .templateVariables(Map.of("matchName", "Final Match"))
        .scheduledAt(LocalDateTime.now().plusHours(2)) // Send in 2 hours
        .build();

NotificationResponse response = client.sendNotification(scheduledRequest);
```

## 🔍 API Reference

### NotificationServiceClient

Main client class for all operations.

#### Methods

| Method | Description | Returns |
|--------|-------------|---------|
| `sendNotification(request)` | Send single notification | `NotificationResponse` |
| `sendBulkNotifications(request)` | Send bulk notifications | `BulkNotificationResponse` |
| `getNotification(id)` | Get notification by ID | `NotificationResponse` |
| `getNotificationsByRecipient(recipient, page, size)` | Query by recipient | `Page<NotificationResponse>` |
| `getNotificationsByStatus(status, page, size)` | Query by status | `Page<NotificationResponse>` |
| `retryNotification(id)` | Retry failed notification | `NotificationResponse` |
| `createTemplate(request)` | Create template | `TemplateResponse` |
| `getTemplate(id)` | Get template | `TemplateResponse` |
| `deleteTemplate(id)` | Delete template | `void` |
| `getMetrics()` | Get current metrics | `MetricsResponse` |
| `getMetricsByRange(from, to)` | Get metrics by range | `MetricsResponse` |
| `getProviderHealthStatus()` | Get provider health | `List<ProviderHealthResponse>` |

## ❗ Error Handling

The SDK provides specific exceptions for different error scenarios:

```java
import org.deepdrimz.drimzodds.notification.sdk.exception.*;

try {
NotificationResponse response = client.sendNotification(request);
} catch (ValidationException e) {
        // Invalid request (400)
        System.err.println("Validation error: " + e.getMessage());
        } catch (NotificationNotFoundException e) {
        // Notification not found (404)
        System.err.println("Not found: " + e.getMessage());
        } catch (ServiceUnavailableException e) {
        // Service unavailable (503)
        System.err.println("Service down: " + e.getMessage());
        } catch (NotificationClientException e) {
        // Other errors
        System.err.println("Error: " + e.getMessage());
        }
```

### Exception Hierarchy

```
NotificationClientException
├── ValidationException
├── NotificationNotFoundException
├── TemplateNotFoundException
└── ServiceUnavailableException
```

## ✅ Best Practices

### 1. Reuse Client Instance

```java
// ✅ Good: Create once, reuse
@Component
public class NotificationService {
    private final NotificationServiceClient client;

    public NotificationService() {
        this.client = NotificationServiceClient.builder()
                .baseUrl("https://api.drimzsport.com")
                .apiKey(System.getenv("NOTIFICATION_API_KEY"))
                .build();
    }

    // Reuse client for all operations
}

// ❌ Bad: Creating new client every time
public void sendNotification() {
    NotificationServiceClient client = NotificationServiceClient.builder()...
}
```

### 2. Use Builder Pattern

```java
// ✅ Good: Fluent and readable
SendNotificationRequest request = SendNotificationRequest.builder()
        .type(NotificationType.EMAIL_VERIFICATION)
        .channel(NotificationChannel.EMAIL)
        .recipient("user@example.com")
        .subject("Verify Email")
        .templateId("template-id")
        .build();

// ❌ Bad: Setting fields manually
SendNotificationRequest request = new SendNotificationRequest();
request.setType(NotificationType.EMAIL_VERIFICATION);
request.setChannel(NotificationChannel.EMAIL);
// ... error-prone
```

### 3. Handle Errors Appropriately

```java
// ✅ Good: Specific error handling
try {
        client.sendNotification(request);
} catch (ValidationException e) {
        // Fix request and retry
        log.error("Invalid request", e);
    return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ServiceUnavailableException e) {
        // Queue for later
        log.warn("Service unavailable, queuing", e);
    queue.add(request);
}

// ❌ Bad: Generic catch
        try {
        client.sendNotification(request);
} catch (Exception e) {
        log.error("Error", e); // Lost context
}
```

### 4. Use Enums for Type Safety

```java
// ✅ Good: Type-safe enums
request.setType(NotificationType.EMAIL_VERIFICATION);
request.setChannel(NotificationChannel.EMAIL);

// ❌ Bad: Magic strings
request.setType("EMAIL_VERIFICATION"); // Compilation error!
```

### 5. Validate Before Sending

The SDK validates automatically, but you can pre-validate:

```java
import org.deepdrimz.drimzodds.notification.sdk.validator.NotificationRequestValidator;

NotificationRequestValidator validator = new NotificationRequestValidator();

try {
        validator.validate(request);
    client.sendNotification(request);
} catch (ValidationException e) {
        // Handle validation error before API call
        }
```

## 🤝 Support

- **Documentation**: [https://docs.drimzsport.com/notification-sdk](https://docs.drimzsport.com/notification-sdk)
- **JavaDoc**: [https://javadoc.drimzsport.com/notification-sdk](https://javadoc.drimzsport.com/notification-sdk)
- **Issues**: [https://github.com/deepdrimz/notification-sdk/issues](https://github.com/deepdrimz/notification-sdk/issues)
- **Email**: support@drimzsport.com

## 📄 License

Proprietary © 2025 DrimzSport. All rights reserved.

---

**Made with ❤️ by the DrimzSport Team**