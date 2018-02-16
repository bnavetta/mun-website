package org.brownmun.db.school;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.brownmun.db.committee.Position;

/**
 * Queries relating to {@link School}s.
 */
public interface SchoolRepository extends JpaRepository<School, Long>
{
    @org.springframework.data.jpa.repository.Query("SELECT d.position from org.brownmun.db.school.Delegate d WHERE d.school = ?1")
    Set<Position> fetchPositions(School school);

    @Query("SELECT d FROM org.brownmun.db.school.Delegate d WHERE d.school = ?1")
    Set<Delegate> fetchDelegates(School school);
}
