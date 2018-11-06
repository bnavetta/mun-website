package org.brownmun.core.award;

import org.brownmun.core.award.model.Award;
import org.brownmun.core.award.model.AwardPrint;
import org.brownmun.core.award.model.AwardType;
import org.brownmun.core.committee.model.Committee;

import java.util.List;
import java.util.Optional;

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

    List<AwardPrint> exportAwards();
}
