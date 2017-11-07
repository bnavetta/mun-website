package org.brownmun.web.admin.awards;

import com.google.common.collect.Lists;
import org.brownmun.model.committee.Award;

import java.util.List;

public class AwardsForm
{
    private long committeeId;
    private List<Award> awards;

    public AwardsForm()
    {
        this.awards = Lists.newArrayList();
    }

    public AwardsForm(long committeeId, List<Award> awards)
    {
        this.committeeId = committeeId;
        this.awards = awards;
    }

    public List<Award> getAwards()
    {
        return awards;
    }

    public void setAwards(List<Award> awards)
    {
        this.awards = awards;
    }

    public long getCommitteeId()
    {
        return committeeId;
    }

    public void setCommitteeId(long committeeId)
    {
        this.committeeId = committeeId;
    }
}
