package org.brownmun.core.committee.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.brownmun.core.committee.model.Position;

/**
 * Queries related to {@link Position}s.
 */
public interface PositionRepository extends JpaRepository<Position, Long>
{
    @Query("SELECT p FROM Position p LEFT JOIN Delegate d ON d.position = p WHERE d IS NULL")
    List<Position> findUnassigned();
}
