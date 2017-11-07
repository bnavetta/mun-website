package org.brownmun.web.admin.awards;

import javax.validation.constraints.Min;
import java.util.List;

public class AwardsBreakdown
{
    private List<Entry> committees;

    public List<Entry> getCommittees()
    {
        return committees;
    }

    public void setCommittees(List<Entry> committees)
    {
        this.committees = committees;
    }

    public static class Entry
    {
        private long committeeId;

        private String committeeName;

        @Min(0)
        private int bestDelegateCount;

        @Min(0)
        private int outstandingDelegateCount;

        @Min(0)
        private int honorableDelegateCount;

        public long getCommitteeId()
        {
            return committeeId;
        }

        public void setCommitteeId(long committeeId)
        {
            this.committeeId = committeeId;
        }

        public String getCommitteeName()
        {
            return committeeName;
        }

        public void setCommitteeName(String committeeName)
        {
            this.committeeName = committeeName;
        }

        public int getBestDelegateCount()
        {
            return bestDelegateCount;
        }

        public void setBestDelegateCount(int bestDelegateCount)
        {
            this.bestDelegateCount = bestDelegateCount;
        }

        public int getOutstandingDelegateCount()
        {
            return outstandingDelegateCount;
        }

        public void setOutstandingDelegateCount(int outstandingDelegateCount)
        {
            this.outstandingDelegateCount = outstandingDelegateCount;
        }

        public int getHonorableDelegateCount()
        {
            return honorableDelegateCount;
        }

        public void setHonorableDelegateCount(int honorableDelegateCount)
        {
            this.honorableDelegateCount = honorableDelegateCount;
        }
    }
}
