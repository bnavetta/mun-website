package org.brownmun.db.logistics;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long>
{
    // TODO: query schools by hotel
}
