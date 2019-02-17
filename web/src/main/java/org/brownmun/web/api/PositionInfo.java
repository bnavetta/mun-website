package org.brownmun.web.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PositionInfo
{
    public static PositionInfo create(long id, String name, String delegateName, String schoolName,
            boolean positionPaper, boolean session1, boolean session2, boolean session3, boolean session4,
            boolean session5)
    {
        return new AutoValue_PositionInfo(id, name, delegateName, schoolName, positionPaper, session1, session2,
                session3, session4, session5);
    }

    @JsonProperty("id")
    public abstract long id();

    @JsonProperty("name")
    public abstract String name();

    @JsonProperty("delegateName")
    public abstract String delegateName();

    @JsonProperty("schoolName")
    public abstract String schoolName();

    @JsonProperty("positionPaper")
    public abstract boolean positionPaper();

    @JsonProperty("session1")
    public abstract boolean session1();

    @JsonProperty("session2")
    public abstract boolean session2();

    @JsonProperty("session3")
    public abstract boolean session3();

    @JsonProperty("session4")
    public abstract boolean session4();

    @JsonProperty("session5")
    public abstract boolean session5();
}
