package org.brownmun.model;

import com.google.common.base.MoreObjects;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

/**
 * Represents a delegate's attendance for their committee.
 */
@Embeddable
public class Attendance implements Serializable
{
    private static final int POSITION_PAPER_INDEX = 0;
    private static final int MIN_SESSION = 1;
    private static final int MAX_SESSION = 4;

    @Convert(converter = BitSetAttributeConverter.class)
    private BitSet attendanceRecord = new BitSet(MAX_SESSION + 1);

    public boolean isPositionPaperSubmitted()
    {
        return attendanceRecord.get(POSITION_PAPER_INDEX);
    }

    public void setPositionPaperSubmitted(boolean submitted)
    {
        attendanceRecord.set(POSITION_PAPER_INDEX, submitted);
    }

    public boolean isPresentForSession(int session)
    {
        checkArgument(session >= MIN_SESSION && session <= MAX_SESSION, "session %s out of bounds", session);
        return attendanceRecord.get(session);
    }

    public void setPresentForSession(int session, boolean present)
    {
        checkArgument(session >= MIN_SESSION && session <= MAX_SESSION, "session %s out of bounds", session);
        attendanceRecord.set(session, present);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(attendanceRecord, that.attendanceRecord);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(attendanceRecord);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("positionPaper", isPositionPaperSubmitted())
                .add("sessionOne", isPresentForSession(1))
                .add("sessionTwo", isPresentForSession(2))
                .add("sessionThree", isPresentForSession(3))
                .add("sessionFour", isPresentForSession(4))
                .toString();
    }
}
