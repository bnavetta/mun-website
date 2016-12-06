package org.brownmun.model.repo;

import org.brownmun.model.Delegate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DelegateRepository extends CrudRepository<Delegate, Long>
{
	@Query(nativeQuery = true, value = "SELECT delegate.* FROM delegate LEFT JOIN position ON delegate.id = position.delegate_id WHERE delegate.school_id = ? AND position.id IS NULL LIMIT 1")
	Optional<Delegate> findFreeDelegate(Long schoolId);
}
