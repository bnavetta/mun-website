package org.brownmun.web.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import org.brownmun.web.common.CommitteeDTO;

@AutoValue
public abstract class CommitteeListing
{
    public static CommitteeListing create(List<CommitteeDTO> general, List<CommitteeDTO> specialized,
            List<CommitteeDTO> crisis, List<CommitteeDTO> jointCrises, Map<Long, Set<CommitteeDTO>> jointCrisisRooms)
    {
        return new AutoValue_CommitteeListing(general, specialized, crisis, jointCrises, jointCrisisRooms);
    }

    @JsonProperty("general")
    abstract List<CommitteeDTO> general();

    @JsonProperty("specialized")
    abstract List<CommitteeDTO> specialized();

    @JsonProperty("crisis")
    abstract List<CommitteeDTO> crisis();

    @JsonProperty("jointCrises")
    abstract List<CommitteeDTO> jointCrises();

    @JsonProperty("jointCrisisRooms")
    abstract Map<Long, Set<CommitteeDTO>> jointCrisisRooms();
}
