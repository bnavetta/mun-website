package org.brownmun.core.school.model;

import java.util.OptionalLong;
import java.util.Set;

import org.brownmun.core.committee.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Queries relating to {@link School}s.
 */
public interface SchoolRepository extends JpaRepository<School, Long>
{
    @Query("SELECT d.position from Delegate d WHERE d.school = ?1")
    Set<Position> fetchPositions(School school);

    @Query("SELECT d FROM Delegate d WHERE d.school = ?1")
    Set<Delegate> fetchDelegates(School school);

    @Query("SELECT d.school.id FROM Delegate d WHERE d.id = ?1")
    OptionalLong findSchoolId(long delegateId);
}
