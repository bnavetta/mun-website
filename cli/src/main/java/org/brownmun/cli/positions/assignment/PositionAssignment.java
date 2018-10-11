package org.brownmun.cli.positions.assignment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.auto.value.AutoValue;

import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.Position;
import org.brownmun.core.school.model.School;

/**
 * Represents the assignment of a specific position to a particular school.
 */
@AutoValue
@JsonPropertyOrder({ "positionId", "positionName", "committeeId", "committeeName", "schoolId", "schoolName" })
public abstract class PositionAssignment
{
    public static Builder builder()
    {
        return new AutoValue_PositionAssignment.Builder();
    }

    @JsonCreator
    public static PositionAssignment create(@JsonProperty("positionId") long positionId, @JsonProperty("positionName") String positionName, @JsonProperty("committeeId") long committeeId, @JsonProperty("committeeName") String committeeName, @JsonProperty("schoolId") long schoolId, @JsonProperty("schoolName") String schoolName)
    {
        return builder()
                .positionId(positionId)
                .positionName(positionName)
                .committeeId(committeeId)
                .committeeName(committeeName)
                .schoolId(schoolId)
                .schoolName(schoolName)
                .build();
    }

    @JsonProperty("positionId")
    public abstract long positionId();

    @JsonProperty("positionName")
    public abstract String positionName();

    @JsonProperty("committeeId")
    public abstract long committeeId();

    @JsonProperty("committeeName")
    public abstract String committeeName();

    @JsonProperty("schoolId")
    public abstract long schoolId();

    @JsonProperty("schoolName")
    public abstract String schoolName();

    @AutoValue.Builder
    abstract static class Builder
    {
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
