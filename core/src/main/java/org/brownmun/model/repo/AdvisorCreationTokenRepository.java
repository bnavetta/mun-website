package org.brownmun.model.repo;

import org.brownmun.model.AdvisorCreationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdvisorCreationTokenRepository extends CrudRepository<AdvisorCreationToken, Long>
{
    Optional<AdvisorCreationToken> findByToken(String token);
}
