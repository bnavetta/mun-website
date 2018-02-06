package org.brownmun.model.repo;

import org.brownmun.model.Delegate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DelegateRepository extends CrudRepository<Delegate, Long>
{
	@Query(nativeQuery = true, value = "SELECT delegate.* FROM delegate LEFT JOIN position ON delegate.id = position.delegate_id WHERE delegate.school_id = ? AND position.id IS NULL LIMIT 1")
	Optional<Delegate> findFreeDelegate(Long schoolId);

	@Query("SELECT new org.brownmun.model.repo.DelegatePosition(d.id, d.name, d.position.name, d.position.committee.name) from Delegate d WHERE d.school.id = ?1 ORDER BY d.position.name ASC")
	List<DelegatePosition> findDelegates(long schoolId);

	@Modifying
	@Query("UPDATE Delegate SET name = ?1 WHERE id = ?2 AND school.id = ?3")
	int nameDelegate(String name, long id, long schoolId);

}
