package org.brownmun.core.school;

import org.brownmun.core.school.model.Advisor;

public interface AdvisorService
{
    void requestPasswordReset(String email) throws PasswordResetException;

    Advisor resetPassword(String code, String newPassword) throws PasswordResetException;

    void changePassword(Advisor advisor, String newPassword);
}
