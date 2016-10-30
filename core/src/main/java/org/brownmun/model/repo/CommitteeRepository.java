package org.brownmun.model.repo;

import org.brownmun.model.Committee;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommitteeRepository extends CrudRepository<Committee, Long>
{
	Optional<Committee> findByName(String name);
}
