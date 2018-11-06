package org.brownmun.core.award.impl;

import org.brownmun.core.award.model.AwardPrint;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Access to the {@code award_print} export view.
 */
public interface AwardPrintRepository extends JpaRepository<AwardPrint, Long> {
}
