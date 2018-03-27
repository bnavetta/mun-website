package org.brownmun.core.logistics;

import java.util.List;

import org.brownmun.core.logistics.model.Hotel;

public interface HotelService
{
    /**
     * Looks up a list of all hotels
     */
    List<Hotel> allHotels();
}
