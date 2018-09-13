package org.brownmun.core.committee.impl;

import java.util.OptionalLong;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.brownmun.core.award.model.Award;
import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.CommitteeType;
import org.brownmun.core.committee.model.Position;
import org.brownmun.core.school.model.Delegate;
import org.brownmun.core.school.model.School;

/** Queries related to {@link Committee}s */
public interface CommitteeRepository extends JpaRepository<Committee, Long>
{
    /**
     * Find the positions that are part of the given committee.
     */
    @Query("SELECT p FROM Position p WHERE p.committee = ?1")
    Set<Position> fetchPositions(Committee committee);

    /**
     * Find all delegates representing a position on the given committee.
     */
    @Query("SELECT p.delegate FROM Position p WHERE p.committee = ?1")
    Set<Delegate> fetchDelegates(Committee committee);

    /**
     * Find all schools that have a delegate on the given committee.
     */
    @Query("SELECT DISTINCT p.delegate.school FROM Position p WHERE p.committee = ?1")
    Set<School> fetchSchools(Committee committee);

    /**
     * Find all awards to delegates on the given committee
     */
    @Query("SELECT a FROM Award a WHERE a.committee = ?1")
    Set<Award> fetchAwards(Committee committee);

    Stream<Committee> findAllByTypeOrderByNameAsc(CommitteeType type);

    @Query("SELECT p.committee.id FROM Position p WHERE p.id = ?1")
    OptionalLong findCommitteeId(long positionId);
}
