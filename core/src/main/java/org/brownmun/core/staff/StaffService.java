package org.brownmun.core.staff;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.brownmun.core.Conference;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.CommitteeType;

/**
 * Helper service for dealing with conference staff.
 */
@Service
public class StaffService
{
    private final Conference conference;
    private final SecretariatProperties secretariat;
    private final CommitteeService committeeService;

    @Autowired
    public StaffService(Conference conference, SecretariatProperties secretariat, CommitteeService committeeService)
    {
        this.conference = conference;
        this.secretariat = secretariat;
        this.committeeService = committeeService;
    }

    /**
     * Tests if the given email address belongs to a secretariat member.
     */
    public boolean isSecretariatEmail(String email)
    {
        return secretariat.getMembers().stream().anyMatch(s -> s.getEmail().equals(email));
    }

    /**
     * Tests if the given email address belongs to a staff member.
     */
    public boolean isStaffEmail(String email)
    {
        return email.endsWith("@" + conference.getDomainName());
    }

    public Set<Long> findAssociatedCommittees(String email)
    {
        int atIndex = email.indexOf('@');
        if (atIndex == -1)
        {
            return Set.of();
        }

        String shortName = email.substring(0, atIndex);

        Set<Long> ids = new HashSet<>();

        committeeService.findByShortName(shortName).ifPresent(committee -> {
            ids.add(committee.getId());
            if (committee.getType() == CommitteeType.JOINT_CRISIS)
            {
                for (Committee room : committeeService.getJointCrisisRooms(committee))
                {
                    ids.add(room.getId());
                }
            }
        });

        return ids;
    }
}
