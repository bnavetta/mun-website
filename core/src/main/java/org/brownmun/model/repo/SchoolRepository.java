package org.brownmun.model.repo;

import org.brownmun.model.RegistrationStatus;
import org.brownmun.model.School;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SchoolRepository extends CrudRepository<School, Long>
{
	Optional<School> findByName(String name);

	int countByStatus(RegistrationStatus status);
}
