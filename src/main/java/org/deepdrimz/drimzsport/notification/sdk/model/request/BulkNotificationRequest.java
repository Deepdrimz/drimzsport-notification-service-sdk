package org.deepdrimz.drimzsport.notification.sdk.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request object for sending multiple notifications in bulk.
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkNotificationRequest {

    /**
     * List of notifications to send.
     */
    @NotEmpty(message = "Notifications list cannot be empty")
    @Valid
    private List<SendNotificationRequest> notifications;
}
