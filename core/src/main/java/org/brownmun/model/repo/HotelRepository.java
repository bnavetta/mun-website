package org.brownmun.model.repo;

import org.brownmun.model.Hotel;
import org.springframework.data.repository.CrudRepository;

public interface HotelRepository  extends CrudRepository<Hotel, Long>
{
}
