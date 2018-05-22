package org.brownmun.web.registration;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RegistrationResult
{
    @JsonProperty
    public abstract long schoolId();

    @JsonProperty
    public abstract long advisorId();

    @JsonProperty
    public abstract URI dashboard();

    public static RegistrationResult create(long schoolId, long advisorId, URI dashboard)
    {
        return new AutoValue_RegistrationResult(schoolId, advisorId, dashboard);
    }
}
