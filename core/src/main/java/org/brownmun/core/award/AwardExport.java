package org.brownmun.core.award;

import java.util.List;

import org.brownmun.core.award.model.AwardPrint;

public final class AwardExport
{
    private final List<AwardPrint> completeAwards;
    private final List<AwardPrint> incompleteAwards;

    public AwardExport(List<AwardPrint> completeAwards, List<AwardPrint> incompleteAwards)
    {
        this.completeAwards = completeAwards;
        this.incompleteAwards = incompleteAwards;
    }

    public List<AwardPrint> getCompleteAwards()
    {
        return completeAwards;
    }

    public List<AwardPrint> getIncompleteAwards()
    {
        return incompleteAwards;
    }
}
