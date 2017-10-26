package org.brownmun.web.admin.printing;

import com.google.common.base.MoreObjects;
import org.brownmun.model.PrintRequest;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Websocket message representation for updates sent to the clients.
 */
public class PrintQueueUpdate
{
    /*
     * TODO: be more clever and send a diff instead of the whole queue
     */

    private List<PrintRequest> queueContents;

    public PrintQueueUpdate(List<PrintRequest> queueContents)
    {
        this.queueContents = queueContents;
    }

    public List<PrintRequest> getQueueContents()
    {
        return queueContents;
    }

    public void setQueueContents(List<PrintRequest> queueContents)
    {
        this.queueContents = queueContents;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintQueueUpdate that = (PrintQueueUpdate) o;
        return Objects.equals(queueContents, that.queueContents);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(queueContents);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("queueContents", queueContents)
                .toString();
    }
}
