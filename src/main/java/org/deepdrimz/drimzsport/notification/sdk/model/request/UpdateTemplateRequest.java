package org.deepdrimz.drimzsport.notification.sdk.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request object for updating an existing notification template.
 * This DTO (Data Transfer Object) is used when modifying template properties
 * through the notification service API.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * UpdateTemplateRequest request = UpdateTemplateRequest.builder()
 *     .name("welcome-email-updated")
 *     .subject("Updated Welcome Message")
 *     .content("Hello {username}, welcome to our platform!")
 *     .language("en")
 *     .requiredVariables(List.of("username"))
 *     .active(true)
 *     .build();
 * }</pre>
 *
 * @author DeepDrimz Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTemplateRequest {

    /**
     * The updated name of the template.
     * <p>
     * This should be a unique, human-readable identifier for the template.
     * Typically follows kebab-case or snake_case conventions (e.g., "welcome-email", "password-reset").
     * </p>
     * <p>
     * <b>Constraints:</b>
     * <ul>
     *   <li>Maximum length: 255 characters</li>
     *   <li>Should only contain alphanumeric characters, hyphens, and underscores</li>
     *   <li>Cannot be null or empty</li>
     * </ul>
     */
    private String name;

    /**
     * The updated subject line for notifications using this template.
     * <p>
     * This is typically used as the email subject or push notification title.
     * Can contain template variables wrapped in curly braces (e.g., "{appName}").
     * </p>
     * <p>
     * <b>Note:</b> If null, the existing subject will not be modified.
     * </p>
     * <p>
     * <b>Constraints:</b>
     * <ul>
     *   <li>Maximum length: 512 characters</li>
     * </ul>
     */
    private String subject;

    /**
     * The updated content/body of the template.
     * <p>
     * This contains the main message content with optional template variables.
     * Variables should be wrapped in curly braces (e.g., "Hello {firstName} {lastName}!").
     * Supports HTML content for email templates.
     * </p>
     * <p>
     * <b>Note:</b> All variables referenced in content must be declared in
     * {@link #requiredVariables}.
     * </p>
     * <p>
     * <b>Constraints:</b>
     * <ul>
     *   <li>Maximum length: 65535 characters</li>
     *   <li>Cannot be null or empty</li>
     * </ul>
     */
    private String content;

    /**
     * The updated language/locale for this template version.
     * <p>
     * Follows ISO 639-1 language codes (e.g., "en", "fr", "es").
     * Used for multilingual notification support.
     * </p>
     * <p>
     * <b>Constraints:</b>
     * <ul>
     *   <li>Exactly 2 lowercase characters</li>
     *   <li>Must be a valid ISO 639-1 code</li>
     * </ul>
     *
     * @see <a href="https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">ISO 639-1 Codes</a>
     */
    private String language;

    /**
     * The updated list of required template variables.
     * <p>
     * These variables must be provided when sending notifications using this template.
     * Each variable name should match the placeholders used in {@link #content} and {@link #subject}.
     * Variable names are case-sensitive and should follow camelCase or snake_case conventions.
     * </p>
     * <p>
     * <b>Example:</b> ["userName", "orderId", "resetLink"]
     * </p>
     * <p>
     * <b>Constraints:</b>
     * <ul>
     *   <li>Maximum 50 variables per template</li>
     *   <li>Variable names: 1-50 characters, alphanumeric and underscores only</li>
     *   <li>Cannot contain null or empty strings</li>
     * </ul>
     */
    private List<String> requiredVariables;

    /**
     * The updated activation status of the template.
     * <p>
     * Determines whether this template can be used for sending notifications:
     * </p>
     * <ul>
     *   <li><b>true:</b> Template is active and can be used</li>
     *   <li><b>false:</b> Template is inactive and cannot be used</li>
     *   <li><b>null:</b> No change to current activation status</li>
     * </ul>
     * <p>
     * <b>Note:</b> At least one template per language must remain active for each template name.
     * </p>
     */
    private Boolean active;

    /**
     * Validates that all required variables are present in the content.
     * This is a business logic validation method.
     *
     * @return true if all required variables are referenced in the content, false otherwise
     * @throws IllegalArgumentException if content is null or empty
     */
    public boolean validateRequiredVariablesInContent() {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        if (requiredVariables == null || requiredVariables.isEmpty()) {
            return true;
        }

        return requiredVariables.stream()
                .allMatch(variable -> content.contains("{" + variable + "}"));
    }

    /**
     * Creates a copy of this request with sanitized input.
     * Trims string fields and removes null/empty entries from collections.
     *
     * @return a sanitized copy of this request
     */
    public UpdateTemplateRequest sanitizedCopy() {
        return UpdateTemplateRequest.builder()
                .name(name != null ? name.trim() : null)
                .subject(subject != null ? subject.trim() : null)
                .content(content != null ? content.trim() : null)
                .language(language != null ? language.trim().toLowerCase() : null)
                .requiredVariables(requiredVariables != null
                        ? requiredVariables.stream()
                        .filter(var -> var != null && !var.trim().isEmpty())
                        .map(String::trim)
                        .toList()
                        : null)
                .active(active)
                .build();
    }
}