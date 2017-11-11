package org.brownmun.web.admin.attendance;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Models the attendance data submitted from the frontend
 */
public class PositionAttendance
{
    private long positionId;
    private String positionName;
    private String schoolName;
    private boolean positionPaper;
    private boolean sessionOne;
    private boolean sessionTwo;
    private boolean sessionThree;
    private boolean sessionFour;

    public long getPositionId()
    {
        return positionId;
    }

    public void setPositionId(long positionId)
    {
        this.positionId = positionId;
    }

    public String getPositionName()
    {
        return positionName;
    }

    public void setPositionName(String positionName)
    {
        this.positionName = positionName;
    }

    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    public boolean isPositionPaper()
    {
        return positionPaper;
    }

    public void setPositionPaper(boolean positionPaper)
    {
        this.positionPaper = positionPaper;
    }

    public boolean isSessionOne()
    {
        return sessionOne;
    }

    public void setSessionOne(boolean sessionOne)
    {
        this.sessionOne = sessionOne;
    }

    public boolean isSessionTwo()
    {
        return sessionTwo;
    }

    public void setSessionTwo(boolean sessionTwo)
    {
        this.sessionTwo = sessionTwo;
    }

    public boolean isSessionThree()
    {
        return sessionThree;
    }

    public void setSessionThree(boolean sessionThree)
    {
        this.sessionThree = sessionThree;
    }

    public boolean isSessionFour()
    {
        return sessionFour;
    }

    public void setSessionFour(boolean sessionFour)
    {
        this.sessionFour = sessionFour;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionAttendance that = (PositionAttendance) o;
        return positionId == that.positionId &&
                positionName == that.positionName &&
                positionPaper == that.positionPaper &&
                sessionOne == that.sessionOne &&
                sessionTwo == that.sessionTwo &&
                sessionThree == that.sessionThree &&
                sessionFour == that.sessionFour;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(positionId, positionName, positionPaper, sessionOne, sessionTwo, sessionThree, sessionFour);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("positionId", positionId)
                .add("positionName", positionName)
                .add("positionPaper", positionPaper)
                .add("sessionOne", sessionOne)
                .add("sessionTwo", sessionTwo)
                .add("sessionThree", sessionThree)
                .add("sessionFour", sessionFour)
                .toString();
    }
}
