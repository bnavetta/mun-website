package org.brownmun.core.school;

import org.brownmun.core.school.model.Advisor;

import java.util.Optional;

public interface AdvisorService
{
    /**
     * Looks up an advisor by ID
     * @param id the advisor ID
     * @return an {@code Optional} containing the advisor, if found
     */
    Optional<Advisor> getAdvisor(long id);

    void requestPasswordReset(String email) throws PasswordResetException;

    Advisor resetPassword(String code, String newPassword) throws PasswordResetException;

    void changePassword(Advisor advisor, String newPassword);

    Advisor createAdvisor(long schoolId, String name, String password, String email, String phoneNumber);
}
