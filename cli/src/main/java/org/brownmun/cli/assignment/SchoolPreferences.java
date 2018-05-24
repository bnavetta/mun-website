package org.brownmun.cli.assignment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

/**
 * A school's preferences as to which kinds of committees they'd like to be in.
 */
@AutoValue
public abstract class SchoolPreferences
{
    @JsonCreator
    public static SchoolPreferences create(@JsonProperty("schoolId") int schoolId, @JsonProperty("name") String name,
            @JsonProperty("general") int general, @JsonProperty("specialized") int specialized,
            @JsonProperty("crisis") int crisis)
    {
        return new AutoValue_SchoolPreferences(schoolId, name, general + specialized + crisis, general, specialized,
                crisis);
    }

    /**
     * The ID of the school in our database.
     */
    public abstract int schoolId();

    /**
     * The school's name
     */
    public abstract String name();

    /**
     * The total number of delegates that the school is bringing.
     */
    public abstract int totalDelegates();

    /**
     * The number of delegates the school would like in GA committees
     */
    public abstract int general();

    /**
     * The number of delegates the school would like in specialized/crisis
     * committees
     */
    public abstract int specialized();

    /**
     * The number of delegates the school would like in crisis committees
     */
    public abstract int crisis();
}
