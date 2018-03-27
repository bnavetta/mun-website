package org.brownmun.web.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Abstraction over a logged-in user, which could be a school advisor or conference staff.
 */
public interface User extends UserDetails
{
    boolean isAdvisor();
    boolean isStaff();
}
