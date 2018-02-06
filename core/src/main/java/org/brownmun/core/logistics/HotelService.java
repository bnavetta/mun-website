package org.brownmun.core.logistics;

import org.brownmun.db.logistics.Hotel;

import java.util.List;

public interface HotelService
{
    /**
     * Looks up a list of all hotels
     */
    List<Hotel> allHotels();
}
