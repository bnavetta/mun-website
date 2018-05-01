package org.brownmun.core.school.impl;

import org.brownmun.core.school.model.Advisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Access to the {@link Advisor} database table.
 */
public interface AdvisorRepository extends JpaRepository<Advisor, Long>
{
    /**
     * Look up one advisor by email address.
     */
    Optional<Advisor> findByEmail(String email);

    @Query("SELECT a FROM Advisor a WHERE a.school.id = ?1")
    List<Advisor> findBySchoolId(long schoolId);
}