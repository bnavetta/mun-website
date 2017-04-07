package org.brownmun.model.delegation;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Delegate Social attendance bands
 */
public enum BandColor
{
    /**
     * Students can only leave the event with an advisor.
     */
    RED,

    /**
     * Students can leave the event with a staff member.
     */
    YELLOW,

    /**
     * The school is not attending.
     */
    NOT_ATTENDING
    ;

    @Override
    public String toString()
    {
        return WordUtils.capitalizeFully(name().replace('_', ' '));
    }
}
