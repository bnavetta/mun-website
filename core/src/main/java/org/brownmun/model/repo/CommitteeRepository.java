package org.brownmun.model.repo;

import org.brownmun.model.Attendance;
import org.brownmun.model.committee.Committee;
import org.brownmun.model.committee.CommitteeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface CommitteeRepository extends CrudRepository<Committee, Long>
{
    Optional<Committee> findByName(String name);

    Collection<Committee> findAllByCommitteeType(CommitteeType type);

    @Query("SELECT c from Committee c WHERE c.committeeType = org.brownmun.model.committee.CommitteeType.CRISIS AND c.jointCrisis = FALSE")
    Collection<Committee> findNonJointCrisisCommittees();

    @Query("SELECT c.id FROM Committee c WHERE c.shortName = ?1")
    Long findIdByShortName(String shortName);
}
