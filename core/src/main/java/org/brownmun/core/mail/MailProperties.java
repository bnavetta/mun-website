package org.brownmun.core.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mail")
public class MailProperties
{
    /**
     * The email address to send messages from.
     */
    private String fromAddress;

    /**
     * The email address to put in the Reply-To header.
     */
    private String replyToAddress;

    public String getFromAddress()
    {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress)
    {
        this.fromAddress = fromAddress;
    }

    public String getReplyToAddress()
    {
        return replyToAddress;
    }

    public void setReplyToAddress(String replyToAddress)
    {
        this.replyToAddress = replyToAddress;
    }
}
