package org.brownmun.web.security;

import org.brownmun.core.school.model.Advisor;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Abstraction over a logged-in user, which could be a school advisor or conference staff.
 */
public interface User extends UserDetails
{
    boolean isAdvisor();
    boolean isStaff();

    /**
     * Checks whether this user can access information about the given school,
     * like the list of students.
     */
    boolean canAccessSchool(long schoolId);

    /**
     * Checks whether this user can modify the given school.
     */
    boolean canModifySchool(long schoolId);

    /**
     * Checks whether this user can access non-public information about the given committee,
     * like its position assignments.
     */
    boolean canAccessCommittee(long committeeId);

    /**
     * Checks whether this user can modify the given committee.
     */
    boolean canModifyCommittee(long committeeId);

    /**
     * Returns the underlying {@link Advisor}.
     * @throws IllegalStateException if this user is not an advisor
     */
    Advisor asAdvisor();

    @Override
    default boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    default boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    default boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    default boolean isEnabled()
    {
        return true;
    }
}
