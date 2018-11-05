package org.brownmun.web.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.brownmun.core.award.model.AwardType;

import javax.annotation.Nullable;
import java.util.OptionalLong;

@AutoValue
public abstract class CommitteeAward
{
    @JsonCreator
    public static CommitteeAward create(@JsonProperty("id") long id, @JsonProperty("type") AwardType type, @JsonProperty("positionId") OptionalLong positionId)
    {
        return new AutoValue_CommitteeAward(id, positionId, type);
    }

    @JsonProperty("id")
    public abstract long id();

    @JsonProperty("positionId")
    public abstract OptionalLong positionId();

    @Nullable
    @JsonProperty("type")
    public abstract AwardType type();
}
