package org.brownmun.cli.positions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.auto.value.AutoValue;

/**
 * Describes how many positions of each type a school was allocated.
 */
@JsonPropertyOrder({ "id", "general", "specialized", "crisis" })
@AutoValue
public abstract class SchoolAllocation
{
    public static SchoolAllocation create(long id, int general, int specialized, int crisis)
    {
        return new AutoValue_SchoolAllocation(id, general, specialized, crisis);
    }

    /**
     * The school's ID in our database
     */
    @JsonProperty
    public abstract long id();

    /**
     * The number of positions in general assembly committees the school was
     * allocated
     */
    @JsonProperty
    public abstract int general();

    /**
     * The number of positions in specialized or historical committees the school
     * was allocated
     */
    @JsonProperty
    public abstract int specialized();

    /**
     * The number of positions in crisis committees the school was allocated
     * (including joint crisis)
     */
    @JsonProperty
    public abstract int crisis();

    /*
     * The total number of delegates that the school is bringing.
     */
    public int totalDelegates()
    {
        return general() + specialized() + crisis();
    }
}
