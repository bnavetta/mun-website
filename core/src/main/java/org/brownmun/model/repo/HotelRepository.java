package org.brownmun.model.repo;

import org.brownmun.model.Hotel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface HotelRepository  extends CrudRepository<Hotel, Long>
{
    @Query("select count(id) from SchoolInfo where hotel = ?1")
    int countSchoolsByHotel(Hotel hotel);

    @Query("select sum(requestedDelegates) from SchoolInfo where hotel = ?1")
    int countDelegatesByHotel(Hotel hotel);

    @Query("select count(id) from SchoolInfo where otherHotel is not null")
    int countSchoolsByOtherHotel();

    @Query("select sum(requestedDelegates) from SchoolInfo where otherHotel is not null")
    int countDelegatesByOtherHotel();
}
