package org.brownmun.core.staff;

import org.brownmun.core.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Helper service for dealing with conference staff.
 */
@Service
public class StaffService
{
    private final Conference conference;
    private final SecretariatProperties secretariat;

    @Autowired
    public StaffService(Conference conference, SecretariatProperties secretariat)
    {
        this.conference = conference;
        this.secretariat = secretariat;
    }

    /**
     * Tests if the given email address belongs to a secretariat member.
     */
    public boolean isSecretariatEmail(String email)
    {
        return secretariat.getMembers().stream().anyMatch(s -> s.getEmailAddress().equals(email));
    }

    /**
     * Tests if the given email address belongs to a staff member.
     */
    public boolean isStaffEmail(String email)
    {
        return email.endsWith("@" + conference.getEmailDomain());
    }
}
