package org.brownmun.core.award;

import org.brownmun.core.award.model.Award;

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
}
