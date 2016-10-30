package org.brownmun.model.repo;

import org.brownmun.model.Position;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PositionRepository extends CrudRepository<Position, Long>
{
	Optional<Position> findByNameAndCommitteeName(String name, String committeeName);
}
