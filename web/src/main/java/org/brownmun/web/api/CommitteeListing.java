package org.brownmun.web.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.brownmun.db.committee.Committee;
import org.brownmun.db.committee.JointCrisis;

import java.util.List;

@AutoValue
public abstract class CommitteeListing
{
    public static CommitteeListing create(List<Committee> general, List<Committee> specialized, List<Committee> crisis, List<JointCrisis> jointCrises)
    {
        return new AutoValue_CommitteeListing(general, specialized, crisis, jointCrises);
    }

    @JsonProperty("general")
    abstract List<Committee> general();

    @JsonProperty("specialized")
    abstract List<Committee> specialized();

    @JsonProperty("crisis")
    abstract List<Committee> crisis();

    @JsonProperty("jointCrises")
    abstract List<JointCrisis> jointCrises();
}
