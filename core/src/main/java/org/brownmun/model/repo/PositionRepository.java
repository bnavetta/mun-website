package org.brownmun.model.repo;

import org.brownmun.model.committee.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long>
{
	Optional<Position> findByNameAndCommitteeName(String name, String committeeName);

	@Query("SELECT p FROM Position p where p.delegate IS NULL")
	Iterable<Position> findUnassignedPositions();
}
