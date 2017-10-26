package org.brownmun.web.security;

import org.brownmun.model.repo.CommitteeRepository;
import org.brownmun.web.general.SecProperties;
import org.brownmun.web.general.SecretariatMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StaffPrincipalExtractor implements PrincipalExtractor
{
    private static final Logger log = LoggerFactory.getLogger(StaffPrincipalExtractor.class);

    private final Set<String> secMembers;
    private final CommitteeRepository committeeRepo;

    @Autowired
    public StaffPrincipalExtractor(SecProperties sec, CommitteeRepository committeeRepo)
    {
        this.secMembers = sec.getMembers().stream().map(SecretariatMember::getEmail).collect(Collectors.toSet());
        this.committeeRepo = committeeRepo;
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map)
    {
        String name = (String) map.get("name");
        String email = (String) map.get("email");

        boolean isSec = secMembers.contains(email);
        Optional<Long> committeeId = Optional.empty();
        if (!isSec)
        {
            String domain = (String) map.get("hd");
            String username = email.substring(0, email.length() - domain.length() - 1);
            committeeId = committeeRepo.findIdByShortName(username);
        }

        StaffMember principal = new StaffMember(name, email, isSec, committeeId);
        log.info("Authenticated staff member {}", principal);
        return principal;
    }
}
