package org.brownmun.core.committee.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import org.brownmun.core.committee.model.Position;

/**
 * Queries related to {@link Position}s.
 */
public interface PositionRepository extends JpaRepository<Position, Long>
{
}
