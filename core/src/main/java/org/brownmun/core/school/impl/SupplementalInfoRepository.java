package org.brownmun.core.school.impl;

import org.brownmun.core.school.model.SupplementalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Database operations for {@link SupplementalInfo}
 */
public interface SupplementalInfoRepository extends JpaRepository<SupplementalInfo, Long>
{
}
