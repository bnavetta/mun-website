package org.brownmun.core.award.impl;

import org.brownmun.core.award.model.Award;
import org.brownmun.core.committee.model.Committee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardRepository extends JpaRepository<Award, Long>
{
    List<Award> findAllByCommittee(Committee c);
}
