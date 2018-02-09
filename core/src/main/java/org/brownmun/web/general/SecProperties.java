package org.brownmun.web.general;

import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("secretariat")
public class SecProperties
{
    private List<SecretariatMember> members = Lists.newArrayList();

    public List<SecretariatMember> getMembers()
    {
        return members;
    }

    public void setMembers(List<SecretariatMember> members)
    {
        this.members = members;
    }
}
