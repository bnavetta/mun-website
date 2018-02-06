package org.brownmun.web.admin.printing;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Model for a Websocket message updating the status of a print request
 */
public class RequestStatusChange
{
    private long requestId;

    public long getRequestId()
    {
        return requestId;
    }

    public void setRequestId(long requestId)
    {
        this.requestId = requestId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestStatusChange that = (RequestStatusChange) o;
        return requestId == that.requestId;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(requestId);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("requestId", requestId)
                .toString();
    }
}
