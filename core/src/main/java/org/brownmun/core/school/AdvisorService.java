package org.brownmun.core.school;

import java.util.Optional;

import org.brownmun.core.school.model.Advisor;

public interface AdvisorService
{
    /**
     * Looks up an advisor by ID
     *
     * @param id the advisor ID
     * @return an {@code Optional} containing the advisor, if found
     */
    Optional<Advisor> getAdvisor(long id);

    void requestPasswordReset(String email) throws PasswordResetException;

    Advisor resetPassword(String code, String newPassword) throws PasswordResetException;

    void changePassword(Advisor advisor, String newPassword);

    Advisor createAdvisor(long schoolId, String name, String password, String email, String phoneNumber);

    /**
     * Update the advisor's {@link Advisor#lastSeen} timestamp
     *
     * @param advisor the advisor to update
     */
    void markSeen(Advisor advisor);
}
