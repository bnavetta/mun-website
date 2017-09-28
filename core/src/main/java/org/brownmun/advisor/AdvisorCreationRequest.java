package org.brownmun.advisor;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * A request to create a new school advisor, with all the required information.
 */
public class AdvisorCreationRequest
{
    private final String name;
    private final String email;

    public AdvisorCreationRequest(String name, String email)
    {
        this.name = name;
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdvisorCreationRequest that = (AdvisorCreationRequest) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, email);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("email", email)
                .toString();
    }
}
