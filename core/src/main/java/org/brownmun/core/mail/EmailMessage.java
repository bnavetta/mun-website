package org.brownmun.core.mail;

import com.google.auto.value.AutoValue;

import java.util.Map;

/**
 * Description of an email.
 */
@AutoValue
public abstract class EmailMessage
{
    /**
     * The address to send the email to
     */
    public abstract String recipient();

    /**
     * The subject of the email
     */
    public abstract String subject();

    /**
     * The name of the message template (relative to {@code src/main/resources/emails}). This should not
     * include the {@code .txt} and {@code .html} extensions - those will be added automatically.
     */
    public abstract String messageTemplate();

    /**
     * Variables to substitute into the message templates
     */
    public abstract Map<String, Object> variables();

    public static Builder builder()
    {
        return new AutoValue_EmailMessage.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder
    {
        public abstract Builder recipient(String recipient);
        public abstract Builder subject(String subject);
        public abstract Builder messageTemplate(String messageTemplate);
        public abstract Builder variables(Map<String, Object> variables);
        public abstract EmailMessage build();
    }
}
