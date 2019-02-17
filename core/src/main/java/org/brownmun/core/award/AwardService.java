package org.brownmun.core.award;

import java.util.List;
import java.util.Optional;

import org.brownmun.core.award.model.Award;
import org.brownmun.core.award.model.AwardType;
import org.brownmun.core.committee.model.Committee;

/**
 * Service abstraction for managing awards.
 */
public interface AwardService
{
    List<Award> awardsForCommittee(long committeeId);

    Award setAward(long awardId, long positionId);

    Award unassignAward(long awardId);

    Optional<Award> getAward(long id);

    void clearAwards();

    Award createAward(Committee committee, AwardType type);

    AwardExport exportAwards();

    List<Award> findUnassignedAwards();
}
