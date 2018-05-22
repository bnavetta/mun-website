package org.brownmun.core.mail.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.base.MoreObjects;

/**
 * Mailgun settings
 */
@ConfigurationProperties("mailgun")
public class MailgunProperties
{
    /**
     * The base API URL. Usually something like
     * {@code https://api.mailgun.netv3/<domain name>}
     */
    private String baseUri;

    /**
     * Username to go along with API key
     */
    private String username = "api";

    /**
     * The Mailgun API key
     */
    private String apiKey;

    public void setBaseUri(String baseUri)
    {
        if (!baseUri.endsWith("/"))
        {
            this.baseUri = baseUri + "/";
        }
        else
        {
            this.baseUri = baseUri;
        }
    }

    public String getBaseUri()
    {
        return baseUri;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("baseUri", baseUri).add("username", username).add("apiKey", apiKey)
                .toString();
    }
}
