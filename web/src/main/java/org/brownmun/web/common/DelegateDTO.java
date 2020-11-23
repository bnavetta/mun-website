package org.brownmun.web.common;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

/**
 * Data transfer object for delegates, used in the dashboard API. This helps
 * control what fields are sent over.
 */
@AutoValue
public abstract class DelegateDTO {
    public static DelegateDTO create(long id, String name, String positionName, String committeeName,
            String gatherlyLink, long positionId) {
        return new AutoValue_DelegateDTO(id, Optional.ofNullable(name), Optional.ofNullable(positionName),
                Optional.ofNullable(committeeName), positionId, Optional.ofNullable(gatherlyLink));
    }

    /**
     * The delegate's ID
     */
    @JsonProperty("id")
    public abstract long id();

    /**
     * The name of the delegate, if set.
     */
    @JsonProperty("name")
    public abstract Optional<String> name();

    /**
     * The name of the position assigned to the delegate.
     */
    @JsonProperty("positionName")
    public abstract Optional<String> positionName();

    /**
     * The name of the committee the delegate was assigned to.
     */
    @JsonProperty("committeeName")
    public abstract Optional<String> committeeName();

    /**
     * The position ID of the position this delegate was assigned to
     */
    @JsonProperty("positionID")
    public abstract long positionID();

    /**
     * BUSUN 2020 gatherly link
     */
    @JsonProperty("gatherlyLink")
    public abstract Optional<String> gatherlyLink();
}
