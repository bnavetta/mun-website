package org.brownmun.model.repo;

import org.brownmun.model.committee.CommitteeType;
import org.brownmun.model.committee.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface PositionRepository extends JpaRepository<Position, Long>
{
	@Query("SELECT p FROM Position p where p.committee.committeeType = ?1")
	Stream<Position> findByCommitteeType(CommitteeType type);

	@Query("SELECT p FROM Position p where p.delegate IS NULL")
	Iterable<Position> findUnassignedPositions();
}
