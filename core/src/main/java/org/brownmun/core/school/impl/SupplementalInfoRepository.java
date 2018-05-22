package org.brownmun.core.school.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import org.brownmun.core.school.model.SupplementalInfo;

/**
 * Database operations for {@link SupplementalInfo}
 */
public interface SupplementalInfoRepository extends JpaRepository<SupplementalInfo, Long>
{
}
