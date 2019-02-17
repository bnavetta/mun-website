package org.brownmun.web.staff;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import org.brownmun.web.security.StaffUser;

/**
 * Information about a staff user
 */
@AutoValue
public abstract class UserInfo
{
    public static UserInfo fromStaffUser(StaffUser user)
    {
        return createUser(user.getEmail(), user.getCommitteeIds(), user.isSecretariat());
    }

    public static UserInfo createUser(String email, Set<Long> committees, boolean isSecretariat)
    {
        return new AutoValue_UserInfo(email, committees, isSecretariat);
    }

    /**
     * The staff member's conference email address.
     */
    @JsonProperty("email")
    public abstract String email();

    /**
     * The committee the staff member represents
     */
    @JsonProperty("committees")
    public abstract Set<Long> committees();

    @JsonProperty("isSecretariat")
    public abstract boolean isSecretariat();
}
