package org.brownmun.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.Map;

/**
 * Conference-specific settings
 */
@ConfigurationProperties("conference")
public class Conference
{
    private String name;
    private String key;
    private String domainName;
    private boolean guidesPublished;
    private UriTemplate backgroundGuideTemplate;

    /**
     * The display name of the conference
     */
    public String getName()
    {
        return name;
    }

    /**
     * A key for lookups
     */
    public String getKey()
    {
        return key;
    }

    /**
     * The conference website domain, used for emails, etc..
     */
    public String getDomainName()
    {
        return domainName;
    }

    /**
     * Whether or not background guides are live on the website.
     */
    public boolean isGuidesPublished() {
        return guidesPublished;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public void setDomainName(String domainName)
    {
        this.domainName = domainName;
    }

    public void setGuidesPublished(boolean guidesPublished) {
        this.guidesPublished = guidesPublished;
    }

    public void setBackgroundGuideTemplate(String template)
    {
        this.backgroundGuideTemplate = new UriTemplate(template);
    }

    /**
     * Get the URL for a committee's background guide
     * @param shortName the short name of the committee, such as {@code undp}.
     * @return a URL to the committee's background guide
     */
    public URI getBackgroundGuideURL(String shortName)
    {
        Map<String, String> params = Map.of("shortName", shortName);
        return backgroundGuideTemplate.expand(params);
    }

    /**
     * Get the email address for contacting a committee's chairs.
     */
    public String getCommitteeEmail(String shortName)
    {
        return shortName + "@" + domainName;
    }
}
