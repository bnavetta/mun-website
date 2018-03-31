package org.brownmun.web.general;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("staff")
public class SecProperties
{
    private List<SecretariatMember> members;

    public List<SecretariatMember> getMembers()
    {
        return members;
    }

    public void setMembers(List<SecretariatMember> members)
    {
        this.members = members;
    }
}
