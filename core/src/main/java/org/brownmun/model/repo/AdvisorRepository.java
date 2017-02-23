package org.brownmun.model.repo;

import org.brownmun.model.Advisor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdvisorRepository extends CrudRepository<Advisor, Long>
{
    Optional<Advisor> findByEmail(String email);
}
