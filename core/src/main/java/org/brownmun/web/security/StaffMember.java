package org.brownmun.web.security;

import com.google.common.base.MoreObjects;

import java.util.Objects;
import java.util.Optional;

public class StaffMember
{
    private String name;
    private String email;

    private boolean isSec;
    private Optional<Long> committeeId;

    public StaffMember(String name, String email, boolean isSec, Optional<Long> committeeId)
    {
        this.name = name;
        this.email = email;
        this.isSec = isSec;
        this.committeeId = committeeId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean isSec()
    {
        return isSec;
    }

    public void setSec(boolean sec)
    {
        isSec = sec;
    }

    public Optional<Long> getCommitteeId()
    {
        return committeeId;
    }

    public void setCommitteeId(Optional<Long> committeeId)
    {
        this.committeeId = committeeId;
    }

    public String getRole()
    {
        if (isSec)
        {
            return "Sec member";
        }
        else if (committeeId.isPresent())
        {
            return "Staff for committee " + committeeId.get();
        }
        else
        {
            return "Staff member";
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffMember that = (StaffMember) o;
        return isSec == that.isSec &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(committeeId, that.committeeId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, email, isSec, committeeId);
    }

    @Override
    public String toString()
    {
        return String.format("%s %s (%s)", getRole(), name, email);
    }
}
