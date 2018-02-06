package org.brownmun.db.committee;

import org.brownmun.db.committee.Award;
import org.brownmun.db.committee.Committee;
import org.brownmun.db.committee.Position;
import org.brownmun.db.school.Delegate;
import org.brownmun.db.school.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * Queries related to {@link Committee}s
 */
public interface CommitteeRepository extends JpaRepository<Committee, Long>
{
    /**
     * Find the positions that are part of the given committee.
     */
    @Query("SELECT p FROM org.brownmun.db.committee.Position p WHERE p.committee = ?1")
    Set<Position> fetchPositions(Committee committee);

    /**
     * Find all delegates representing a position on the given committee.
     */
    @Query("SELECT p.delegate FROM org.brownmun.db.committee.Position p WHERE p.committee = ?1")
    Set<Delegate> fetchDelegates(Committee committee);

    /**
     * Find all schools that have a delegate on the given committee.
     */
    @Query("SELECT DISTINCT p.delegate.school FROM org.brownmun.db.committee.Position p WHERE p.committee = ?1")
    Set<School> fetchSchools(Committee committee);

    /**
     * Find all awards to delegates on the given committee
     */
    @Query("SELECT a FROM Award a WHERE a.committee = ?1")
    Set<Award> fetchAwards(Committee committee);

    List<Committee> findAllByTypeOrderByNameAsc(CommitteeType type);

    @Query("SELECT c FROM Committee c LEFT JOIN JointCrisis j ON c MEMBER OF j.committees WHERE j IS NULL AND c.type = org.brownmun.db.committee.CommitteeType.CRISIS ORDER BY c.name ASC")
    List<Committee> findNonJointCrises();
}
