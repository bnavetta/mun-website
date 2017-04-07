package org.brownmun.model.delegation;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Times at which the delegation needs shuttle service
 */
@Data
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
}
