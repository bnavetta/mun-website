package org.brownmun.mail.mailgun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mailgun settings
 */
@ConfigurationProperties("mailgun")
@Data
public class MailgunProperties
{
    /**
     * The base API URL. Usually something like {@code https://api.mailgun.netv3/<domain name>}
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
}
