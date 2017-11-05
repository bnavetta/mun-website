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

    private final StaffService staff;

    @Autowired
    public StaffPrincipalExtractor(StaffService staff)
    {
        this.staff = staff;
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map)
    {
        String email = (String) map.get("email");

        Optional<StaffMember> principal = staff.getByEmail(email);
        log.info("Found staff member {} for email {}", principal, email);
        return principal.orElse(null);
    }
}
