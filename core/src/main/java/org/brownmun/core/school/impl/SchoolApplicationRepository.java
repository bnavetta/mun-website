package org.brownmun.core.school.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import org.brownmun.core.school.model.SchoolApplication;

/**
 * CRUD interface for the {@link SchoolApplication} database view.
 */
public interface SchoolApplicationRepository extends JpaRepository<SchoolApplication, Long>
{
}
