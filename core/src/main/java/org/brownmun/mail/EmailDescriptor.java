package org.brownmun.mail;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.core.io.Resource;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Models an email to send, potentially batched.
 */
@Data
public class EmailDescriptor
{
    /**
     * The email address to send the message from
     */
    private String from;

    /**
     * Recipients of the message. Keys of this map are recipient email
     * addresses and values are maps of recipient variables.
     */
    private Map<String, Map<String, String>> recipients;

    /**
     * Reply-to email address
     */
    private Optional<String> replyTo = Optional.empty();

    /**
     * The email subject, which may contain recipient variables.
     */
    private String subject;

    /**
     * The text content of the email, which may contain recipient variables.
     */
    private Optional<String> text = Optional.empty();

    /**
     * The HTML content of the email, which may contain recipient variables.
     */
    private Optional<String> html = Optional.empty();

    /**
     * Spring {@link Resource}s to include as inline content. Each will be assigned
     * a content ID.
     */
    private Collection<Resource> inline = Lists.newLinkedList();

    /**
     * Spring {@link Resource}s to send as attachments to the email.
     */
    private Collection<Resource> attachments = Lists.newLinkedList();

    /**
     * Tags to label the emails with (if supported by the service).
     */
    private Set<String> tags = Sets.newHashSet();

    public void setHtml(String html)
    {
        this.html = Optional.ofNullable(html);
    }

    public void setText(String text)
    {
        this.text = Optional.ofNullable(text);
    }
}
