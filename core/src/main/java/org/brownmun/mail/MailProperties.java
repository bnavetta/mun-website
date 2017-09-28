package org.brownmun.mail;

import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Settings for email sending.
 */
@ConfigurationProperties("mail")
public class MailProperties
{
    String templateLocation;

    String linkBase;

    String fromAddress;
    String replyAddress;

    public String getTemplateLocation()
    {
        return templateLocation;
    }

    public void setTemplateLocation(String templateLocation)
    {
        this.templateLocation = templateLocation;
    }

    public String getLinkBase()
    {
        return linkBase;
    }

    public void setLinkBase(String linkBase)
    {
        this.linkBase = linkBase;
    }

    public String getFromAddress()
    {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress)
    {
        this.fromAddress = fromAddress;
    }

    public String getReplyAddress()
    {
        return replyAddress;
    }

    public void setReplyAddress(String replyAddress)
    {
        this.replyAddress = replyAddress;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("templateLocation", templateLocation)
                .add("linkBase", linkBase)
                .add("fromAddress", fromAddress)
                .add("replyAddress", replyAddress)
                .toString();
    }
}
