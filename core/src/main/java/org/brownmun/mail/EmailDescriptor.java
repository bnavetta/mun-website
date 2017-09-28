package org.brownmun.mail;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.core.io.Resource;

import java.util.*;

/**
 * Models an email to send, potentially batched.
 */
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

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public Map<String, Map<String, String>> getRecipients()
    {
        return recipients;
    }

    public void setRecipients(Map<String, Map<String, String>> recipients)
    {
        this.recipients = recipients;
    }

    public Optional<String> getReplyTo()
    {
        return replyTo;
    }

    public void setReplyTo(Optional<String> replyTo)
    {
        this.replyTo = replyTo;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public Optional<String> getText()
    {
        return text;
    }

    public void setText(Optional<String> text)
    {
        this.text = text;
    }

    public Optional<String> getHtml()
    {
        return html;
    }

    public void setHtml(Optional<String> html)
    {
        this.html = html;
    }

    public Collection<Resource> getInline()
    {
        return inline;
    }

    public void setInline(Collection<Resource> inline)
    {
        this.inline = inline;
    }

    public Collection<Resource> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(Collection<Resource> attachments)
    {
        this.attachments = attachments;
    }

    public Set<String> getTags()
    {
        return tags;
    }

    public void setTags(Set<String> tags)
    {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailDescriptor that = (EmailDescriptor) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(recipients, that.recipients) &&
                Objects.equals(replyTo, that.replyTo) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(text, that.text) &&
                Objects.equals(html, that.html) &&
                Objects.equals(inline, that.inline) &&
                Objects.equals(attachments, that.attachments) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(from, recipients, replyTo, subject, text, html, inline, attachments, tags);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("from", from)
                .add("recipients", recipients)
                .add("replyTo", replyTo)
                .add("subject", subject)
                .add("text", text)
                .add("html", html)
                .add("inline", inline)
                .add("attachments", attachments)
                .add("tags", tags)
                .toString();
    }
}
