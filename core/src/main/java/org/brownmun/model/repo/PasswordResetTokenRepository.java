package org.brownmun.model.repo;

import org.brownmun.model.Advisor;
import org.brownmun.model.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Repository interface for {@link PasswordResetToken}s.
 */
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, String>
{
    Collection<PasswordResetToken> findByAdvisor(Advisor advisor);
}
