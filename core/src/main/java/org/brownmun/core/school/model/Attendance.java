package org.brownmun.core.school.model;

import javax.persistence.Embeddable;

import com.google.common.base.MoreObjects;

/**
 * A delegate's attendance record, tracking which sessions they've been to.
 */
@Embeddable
public class Attendance
{
    /**
     * Did they submit their position paper?
     */
    private boolean positionPaperSubmitted;

    /*
     * A flag for each session, {@code true} if the delegate was present.
     */

    private boolean sessionOne;
    private boolean sessionTwo;
    private boolean sessionThree;
    private boolean sessionFour;
    private boolean sessionFive;

    public boolean isPositionPaperSubmitted()
    {
        return positionPaperSubmitted;
    }

    public void setPositionPaperSubmitted(boolean positionPaperSubmitted)
    {
        this.positionPaperSubmitted = positionPaperSubmitted;
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

    public boolean isSessionFive()
    {
        return sessionFive;
    }

    public void setSessionFive(boolean sessionFive)
    {
        this.sessionFive = sessionFive;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("positionPaperSubmitted", positionPaperSubmitted)
                .add("sessionOne", sessionOne).add("sessionTwo", sessionTwo).add("sessionThree", sessionThree)
                .add("sessionFour", sessionFour).add("sessionFive", sessionFive).toString();
    }
}
