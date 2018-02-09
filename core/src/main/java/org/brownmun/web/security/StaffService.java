package org.brownmun.web.security;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.brownmun.model.repo.CommitteeRepository;
import org.brownmun.web.general.SecProperties;
import org.brownmun.web.general.SecretariatMember;
import org.brownmun.web.security.StaffMember.StaffType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StaffService
{
    private final CommitteeRepository committeeRepo;
    private final ImmutableMap<String, StaffMember> staff;

    public StaffService(CommitteeRepository committeeRepo, SecProperties sec, StaffProperties staffers)
    {
        this.committeeRepo = committeeRepo;
        this.staff = generateStaff(sec, staffers);
    }

    /**
     * Look up a staff member by email address
     * @param email the staff member's email address (i.e. sec email, chair email, or registered ops staffer email)
     * @return the staff member, or an empty Optional if none was found
     */
    public Optional<StaffMember> getByEmail(String email)
    {
        return Optional.ofNullable(staff.get(email));
    }

    private ImmutableMap<String, StaffMember> generateStaff(SecProperties sec, StaffProperties staffers)
    {
        Map<String, StaffMember> staff = Maps.newHashMap();

        System.out.println("SECRETARIAT: " + sec);
        for(SecretariatMember secMember : sec.getMembers())
        {
            StaffMember secStaff = new StaffMember(secMember.getEmail(), StaffType.SECRETARIAT, -1);
            staff.put(secMember.getEmail(), secStaff);
        }

        for (String opsEmail : staffers.getOps())
        {
            staff.put(opsEmail, new StaffMember(opsEmail, StaffType.OPS, -1));
        }

        for (Map.Entry<String, List<String>> committee : staffers.getCommittees().entrySet())
        {
            Long id = committeeRepo.findIdByShortName(committee.getKey());
            if (id == null)
            {
                throw new IllegalStateException("No committee with short name " + committee.getKey());
            }
            for (String staffEmail : committee.getValue())
            {
                staff.put(staffEmail, new StaffMember(staffEmail, StaffType.COMMITTEE, id));
            }
        }

        return ImmutableMap.copyOf(staff);
    }
}
