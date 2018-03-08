package org.brownmun.web.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import org.brownmun.db.committee.Committee;
import org.brownmun.db.committee.JointCrisis;

@AutoValue
public abstract class CommitteeListing
{
    public static CommitteeListing create(List<Committee> general, List<Committee> specialized, List<Committee> crisis,
            List<JointCrisis> jointCrises)
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
