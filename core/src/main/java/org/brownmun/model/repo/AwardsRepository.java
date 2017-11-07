package org.brownmun.model.repo;

import org.brownmun.model.committee.Award;
import org.brownmun.model.committee.AwardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AwardsRepository extends JpaRepository<Award, Long>
{
    @Query("SELECT a.committee.id AS committeeId, a.committee.name AS committeeName, a.id AS awardId, a.awardType AS awardType, a.position.id AS positionId, a.position.name AS positionName, a.position.delegate.name AS delegateName, a.position.delegate.school.id AS schoolId, a.position.delegate.school.name AS schoolName FROM Award a ORDER BY committeeName")
    List<AwardInfo> findAwardInfo();
}
