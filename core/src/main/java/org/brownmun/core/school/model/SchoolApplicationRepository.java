package org.brownmun.core.school.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CRUD interface for the {@link SchoolApplication} database view.
 */
public interface SchoolApplicationRepository extends JpaRepository<SchoolApplication, Long>
{
}
