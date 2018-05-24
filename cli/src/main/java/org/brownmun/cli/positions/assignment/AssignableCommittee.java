package org.brownmun.cli.positions.assignment;

import com.google.auto.value.AutoValue;

/**
 * Representation of a committee as needed for assigning positions.
 */
@AutoValue
public abstract class AssignableCommittee
{
    public static AssignableCommittee create(long id, int capacity)
    {
        return new AutoValue_AssignableCommittee(id, capacity);
    }

    /**
     * The committee's ID.
     */
    public abstract long id();

    /**
     * The number of positions in the committee.
     */
    public abstract int capacity();
}
