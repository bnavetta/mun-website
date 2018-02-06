package org.brownmun.model.delegation;

import org.apache.commons.lang3.text.WordUtils;

/**
 * When a school needs their luggage stored
 */
public enum LuggageStorage
{
    FRIDAY,
    SUNDAY,
    NONE,
    BOTH_DAYS
    ;

    @Override
    public String toString()
    {
        return WordUtils.capitalizeFully(name().replace('_', ' '));
    }
}
