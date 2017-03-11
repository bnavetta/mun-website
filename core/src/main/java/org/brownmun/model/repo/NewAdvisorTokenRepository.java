package org.brownmun.model.repo;

import org.brownmun.model.NewAdvisorToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NewAdvisorTokenRepository extends CrudRepository<NewAdvisorToken, Long>
{
    public Optional<NewAdvisorToken> findByToken(String token);
}
