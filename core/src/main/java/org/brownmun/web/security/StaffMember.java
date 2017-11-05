package org.brownmun.web.security;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.Objects;
import java.util.Optional;

public class StaffMember
{
    private String email;

    private StaffType type;
    private long committeeId;

    public StaffMember(String email, StaffType staffType, long committeeId)
    {
        this.email = email;
        this.type = staffType;
        this.committeeId = committeeId;

        if (staffType == StaffType.COMMITTEE && committeeId < 1)
        {
            throw new IllegalArgumentException("Committee staff must have a committee ID");
        }
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public StaffType getType()
    {
        return type;
    }

    public void setType(StaffType type)
    {
        this.type = type;
    }

    public long getCommitteeId()
    {
        return committeeId;
    }

    public void setCommitteeId(long committeeId)
    {
        this.committeeId = committeeId;
    }

    public boolean isCommitteeStaff()
    {
        return committeeId > 0;
    }

    public String getRole()
    {
        switch (type)
        {
            case SECRETARIAT:
                return "Sec member";
            case COMMITTEE:
                return "Committee " + committeeId + " staff";
            case OPS:
                return "Ops staff";
        }

        return null;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffMember that = (StaffMember) o;
        return committeeId == that.committeeId &&
                Objects.equals(email, that.email) &&
                type == that.type;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(email, type, committeeId);
    }

    @Override
    public String toString()
    {
        return String.format("%s (%s)", email, getRole());
    }

    public enum StaffType
    {
        SECRETARIAT,
        OPS,
        COMMITTEE
    }
}
