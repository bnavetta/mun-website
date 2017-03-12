package org.brownmun.model.repo;

import org.brownmun.model.Attendance;
import org.brownmun.model.Committee;
import org.brownmun.model.CommitteeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface CommitteeRepository extends CrudRepository<Committee, Long>
{
    Optional<Committee> findByName(String name);

    @Query("SELECT a from Attendance a JOIN a.delegate WHERE a.delegate.position.committee = ?1")
    Collection<Attendance> findAttendance(Committee committee);

    Collection<Committee> findAllByCommitteeType(CommitteeType type);
}
