package org.brownmun.core.award.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import org.brownmun.core.award.model.AwardPrint;

/**
 * Access to the {@code award_print} export view.
 */
public interface AwardPrintRepository extends JpaRepository<AwardPrint, Long>
{
}
