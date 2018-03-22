package org.brownmun.core.secretariat;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Defines secretariat-related configuration. This is only the list of members
 * for now.
 */
@ConfigurationProperties("secretariat")
public class SecretariatProperties
{
    private List<SecretariatMember> members = List.of();

    public List<SecretariatMember> getMembers()
    {
        return members;
    }

    public void setMembers(List<SecretariatMember> members)
    {
        this.members = members;
    }
}
