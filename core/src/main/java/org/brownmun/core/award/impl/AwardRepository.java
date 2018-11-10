package org.brownmun.core.award.impl;

import org.brownmun.core.award.model.Award;
import org.brownmun.core.committee.model.Committee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AwardRepository extends JpaRepository<Award, Long>
{
    List<Award> findAllByCommittee(Committee c);

    @Query("SELECT a FROM Award a WHERE a.position IS NULL ORDER BY a.committee.type, a.committee.name, a.type ASC")
    List<Award> findUnassigned();
}
