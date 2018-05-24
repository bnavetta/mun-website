package org.brownmun.cli.positions.assignment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.primitives.ImmutableLongArray;

import java.util.Collection;

/**
 * Inputs to the positions algorithm.
 */
@AutoValue
public abstract class AssignmentSettings
{
    @JsonCreator
    public static AssignmentSettings create(@JsonProperty("generalOverlap") int generalOverlap, @JsonProperty("specializedOverlap") int specializedOverlap, @JsonProperty("crisisOverlap") int crisisOverlap, @JsonProperty("midnightCrisisId") long midnightCrisisId, @JsonProperty("nonMidnightCrisisSchools") Collection<Long> midnightCrisisSchools)
    {
        return new AutoValue_AssignmentSettings(generalOverlap, specializedOverlap, crisisOverlap, midnightCrisisId, ImmutableLongArray.copyOf(midnightCrisisSchools));
    }

    /**
     * The most students from the same school that can be in a GA committee together.
     */
    @JsonProperty
    public abstract int generalOverlap();

    /**
     * The most students from the same school that can be in a spec committee together.
     */
    @JsonProperty
    public abstract int specializedOverlap();

    /**
     * The most students from the same school that can be in a crisis committee together.
     */
    @JsonProperty
    public abstract int crisisOverlap();

    /**
     * The ID of the midnight crisis committee.
     */
    @JsonProperty
    public abstract long midnightCrisisId();

    /**
     * The IDs of schools we can't assign to the midnight crisis committee.
     */
    @JsonProperty
    public abstract ImmutableLongArray nonMidnightCrisisSchools();
}
