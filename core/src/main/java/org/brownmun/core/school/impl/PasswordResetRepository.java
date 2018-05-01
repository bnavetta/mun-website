package org.brownmun.core.school.impl;

import org.brownmun.core.school.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, String>
{
}
