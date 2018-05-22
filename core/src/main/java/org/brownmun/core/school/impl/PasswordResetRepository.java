package org.brownmun.core.school.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import org.brownmun.core.school.model.PasswordReset;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, String>
{
}
