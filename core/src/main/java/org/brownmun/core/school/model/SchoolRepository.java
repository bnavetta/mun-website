package org.brownmun.core.school.model;

import java.util.Set;

import org.brownmun.core.committee.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Queries relating to {@link School}s.
 */
public interface SchoolRepository extends JpaRepository<School, Long>
{
    @org.springframework.data.jpa.repository.Query("SELECT d.position from Delegate d WHERE d.school = ?1")
    Set<Position> fetchPositions(School school);

    @Query("SELECT d FROM Delegate d WHERE d.school = ?1")
    Set<Delegate> fetchDelegates(School school);
}
