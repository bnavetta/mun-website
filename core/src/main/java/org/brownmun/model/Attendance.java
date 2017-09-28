package org.brownmun.model;

import com.google.common.base.MoreObjects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

/**
 * Represents a delegate's attendance for their committee.
 */
@Entity
public class Attendance implements Serializable
{
    private static final int POSITION_PAPER_INDEX = 0;
    private static final int MIN_SESSION = 1;
    private static final int MAX_SESSION = 5;

    @Id
    @OneToOne
    @JoinColumn(name = "delegate_id", referencedColumnName = "id")
    private Delegate delegate;

    // TODO: link to staff member since they have no local state
    // is it needed?

    // TODO: just do a bitset where i == session (with 0 for position paper)

    private BitSet record = new BitSet(MAX_SESSION + 1);

    public boolean positionPaperSubmitted()
    {
        return record.get(POSITION_PAPER_INDEX);
    }

    public void setPositionPaperSubmitted(boolean submitted)
    {
        record.set(POSITION_PAPER_INDEX, submitted);
    }

    public boolean isPresentForSession(int session)
    {
        checkElementIndex(session, MAX_SESSION + 1, "%s beyond last session");
        checkArgument(session >= MIN_SESSION, "%s before first session", session);
        return record.get(session);
    }

    public void setPresentForSession(int session, boolean present)
    {
        checkElementIndex(session, MAX_SESSION + 1, "%s beyond last session");
        checkArgument(session >= MIN_SESSION, "%s before first session", session);
        record.set(session, present);
    }

    public Delegate getDelegate()
    {
        return delegate;
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(record, that.record);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(record);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("delegate", delegate)
                .add("record", record)
                .toString();
    }
}
