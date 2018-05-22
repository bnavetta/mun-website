package org.brownmun.core.committee;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import org.brownmun.core.committee.model.Committee;

@AutoValue
public abstract class CommitteeListing
{
    public static CommitteeListing create(List<Committee> general, List<Committee> specialized, List<Committee> crisis,
            List<Committee> jointCrises, Map<Long, Set<Committee>> jointCrisisRooms)
    {
        return null;
//        return new AutoValue_CommitteeListing(general, specialized, crisis, jointCrises, jointCrisisRooms);
    }

    @JsonProperty("general")
    abstract List<Committee> general();

    @JsonProperty("specialized")
    abstract List<Committee> specialized();

    @JsonProperty("crisis")
    abstract List<Committee> crisis();

    @JsonProperty("jointCrises")
    abstract List<Committee> jointCrises();

    @JsonProperty("jointCrisisRooms")
    abstract Map<Long, Set<Committee>> jointCrisisRooms();
}
