package org.brownmun.core.committee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.brownmun.db.committee.CommitteeDisplay;
import org.brownmun.db.committee.JointCrisis;

import java.util.List;

@AutoValue
public abstract class CommitteeListing
{
    public static CommitteeListing create(List<CommitteeDisplay> general, List<CommitteeDisplay> specialized, List<CommitteeDisplay> crisis,
                                          List<JointCrisis> jointCrises)
    {
        return new AutoValue_CommitteeListing(general, specialized, crisis, jointCrises);
    }

    @JsonProperty("general")
    abstract List<CommitteeDisplay> general();

    @JsonProperty("specialized")
    abstract List<CommitteeDisplay> specialized();

    @JsonProperty("crisis")
    abstract List<CommitteeDisplay> crisis();

    @JsonProperty("jointCrises")
    abstract List<JointCrisis> jointCrises();
}
