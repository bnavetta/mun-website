package org.brownmun.cli.positions.assignment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.primitives.ImmutableLongArray;

/**
 * Inputs to the positions algorithm.
 */
@AutoValue
public abstract class AssignmentSettings
{
    @JsonCreator
    public static AssignmentSettings create(@JsonProperty("generalOverlap") int generalOverlap,
            @JsonProperty("specializedOverlap") int specializedOverlap,
            @JsonProperty("crisisOverlap") int crisisOverlap,
            @JsonProperty("reservedPositions") long[] reservedPositions)
    {
        return new AutoValue_AssignmentSettings(generalOverlap, specializedOverlap, crisisOverlap,
                ImmutableLongArray.copyOf(reservedPositions));
    }

    /**
     * The most students from the same school that can be in a GA committee
     * together.
     */
    @JsonProperty
    public abstract int generalOverlap();

    /**
     * The most students from the same school that can be in a spec committee
     * together.
     */
    @JsonProperty
    public abstract int specializedOverlap();

    /**
     * The most students from the same school that can be in a crisis committee
     * together.
     */
    @JsonProperty
    public abstract int crisisOverlap();

    /**
     * Positions that shouldn't be assigned, such as those set aside for PASA.
     */
    @JsonProperty
    public abstract ImmutableLongArray reservedPositions();
}
