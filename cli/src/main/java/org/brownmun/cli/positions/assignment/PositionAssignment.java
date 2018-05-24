package org.brownmun.cli.positions.assignment;

import com.google.auto.value.AutoValue;
import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.Position;
import org.brownmun.core.school.model.School;

/**
 * Represents the assignment of a specific position to a particular school.
 */
@AutoValue
public abstract class PositionAssignment
{
    public static Builder builder()
    {
        return new AutoValue_PositionAssignment.Builder();
    }

    public abstract long positionId();
    public abstract String positionName();

    public abstract long committeeId();
    public abstract String committeeName();

    public abstract long schoolId();
    public abstract String schoolName();

    @AutoValue.Builder
    abstract static class Builder {
        public abstract Builder positionId(long positionId);
        public abstract Builder positionName(String positionName);
        public abstract Builder committeeId(long committeeId);
        public abstract Builder committeeName(String committeeName);
        public abstract Builder schoolId(long schoolId);
        public abstract Builder schoolName(String schoolName);

        public Builder withPosition(Position p)
        {
            return positionId(p.getId()).positionName(p.getName());
        }

        public Builder withCommittee(Committee c)
        {
            return committeeId(c.getId()).committeeName(c.getName());
        }

        public Builder withSchool(School s)
        {
            return schoolId(s.getId()).schoolName(s.getName());
        }

        public abstract PositionAssignment build();
    }
}
