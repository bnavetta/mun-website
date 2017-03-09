package org.brownmun.model.repo;

import org.brownmun.model.LineItem;
import org.brownmun.model.School;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import javax.persistence.OrderBy;

/**
 * Spring Data repository interface for {@link LineItem} entities.
 */
public interface LineItemRepository extends CrudRepository<LineItem, Long>
{
    @OrderBy("timestamp ASC")
    Collection<LineItem> findBySchoolAndType(School school, LineItem.Type type);

    @OrderBy("timestamp ASC")
    Collection<LineItem> findBySchool(School school);
}
