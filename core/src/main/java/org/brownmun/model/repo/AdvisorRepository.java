package org.brownmun.model.repo;

import org.brownmun.model.advisor.Advisor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdvisorRepository extends CrudRepository<Advisor, Long>
{
    @EntityGraph(attributePaths = { "school" })
    Optional<Advisor> findByEmailIgnoreCase(String email);
}
