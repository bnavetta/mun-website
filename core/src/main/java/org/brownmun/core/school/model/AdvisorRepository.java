package org.brownmun.core.school.model;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
