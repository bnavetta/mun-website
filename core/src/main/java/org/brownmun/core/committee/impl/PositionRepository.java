package org.brownmun.core.committee.impl;

import org.brownmun.core.committee.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Queries related to {@link Position}s.
 */
public interface PositionRepository extends JpaRepository<Position, Long> {
}
