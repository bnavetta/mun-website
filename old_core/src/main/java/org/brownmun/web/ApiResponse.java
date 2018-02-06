package org.brownmun.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Generic API response model
 */
public class ApiResponse
{
    private final boolean ok;
    private final String message;

    @JsonCreator
    public ApiResponse(@JsonProperty("ok") boolean ok, @JsonProperty("message") String message)
    {
        this.ok = ok;
        this.message = message;
    }

    public boolean isOk()
    {
        return ok;
    }

    public String getMessage()
    {
        return message;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiResponse that = (ApiResponse) o;
        return ok == that.ok &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(ok, message);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("ok", ok)
                .add("message", message)
                .toString();
    }
}
