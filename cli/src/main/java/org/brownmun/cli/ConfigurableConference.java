package org.brownmun.cli;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.brownmun.core.Conference;

@ConfigurationProperties("conference")
public class ConfigurableConference implements Conference
{
    private String name;
    private String key;
    private String domainName;

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getKey()
    {
        return key;
    }

    @Override
    public String getDomainName()
    {
        return domainName;
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
}
