package org.brownmun.model.repo;

import org.brownmun.model.RegistrationStatus;
import org.brownmun.model.School;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SchoolRepository extends CrudRepository<School, Long>
{
    Optional<School> findByName(String name);

    int countByStatus(RegistrationStatus status);

    @Query("SELECT COALESCE(SUM(s.info.requestedDelegates), 0) FROM School s WHERE s.status = ?1")
    int countRequestedDelegatesByStatus(RegistrationStatus status);

    @Query("SELECT COALESCE(SUM(s.info.requestedDelegates), 0) FROM School s")
    int countRequestedDelegates();

    @EntityGraph(attributePaths = {"advisors"})
    School findById(Long id);
}
