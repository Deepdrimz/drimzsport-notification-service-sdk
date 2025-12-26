package org.deepdrimz.drimzsport.notification.sdk.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request object for updating an existing template.
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTemplateRequest {
    private String name;
    private String subject;
    private String content;
    private String language;
    private List<String> requiredVariables;
    private Boolean active;
}
