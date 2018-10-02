package org.brownmun.core.school.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import org.brownmun.core.school.model.Delegate;

/**
 * Access to the {@link Delegate} database table.
 */
public interface DelegateRepository extends JpaRepository<Delegate, Long>
{
}
