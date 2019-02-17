package org.brownmun.web.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AttendanceRequest
{
    @JsonCreator
    public static AttendanceRequest create(@JsonProperty("positionId") long positionId,
            @JsonProperty("session") String session, @JsonProperty("present") boolean present)
    {
        return new AutoValue_AttendanceRequest(positionId, session, present);
    }

    public abstract long positionId();

    public abstract String session();

    public abstract boolean present();
}
