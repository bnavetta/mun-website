package org.brownmun.model.delegation;

import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Times at which the delegation needs shuttle service
 */
@Embeddable
public class ShuttleService
{
    @Column(name = "shuttle_friday_afternoon")
    private boolean fridayAfternoon;

    @Column(name = "shuttle_friday_night")
    private boolean fridayNight;

    @Column(name = "shuttle_saturday")
    private boolean saturday;

    @Column(name = "shuttle_sunday")
    private boolean sunday;

    @Transient
    public boolean isNone()
    {
        return !fridayAfternoon && !fridayNight && !saturday && !sunday;
    }

    public boolean isFridayAfternoon()
    {
        return fridayAfternoon;
    }

    public void setFridayAfternoon(boolean fridayAfternoon)
    {
        this.fridayAfternoon = fridayAfternoon;
    }

    public boolean isFridayNight()
    {
        return fridayNight;
    }

    public void setFridayNight(boolean fridayNight)
    {
        this.fridayNight = fridayNight;
    }

    public boolean isSaturday()
    {
        return saturday;
    }

    public void setSaturday(boolean saturday)
    {
        this.saturday = saturday;
    }

    public boolean isSunday()
    {
        return sunday;
    }

    public void setSunday(boolean sunday)
    {
        this.sunday = sunday;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("fridayAfternoon", fridayAfternoon)
                .add("fridayNight", fridayNight)
                .add("saturday", saturday)
                .add("sunday", sunday)
                .toString();
    }
}
