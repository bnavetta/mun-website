package org.brownmun.web.security;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.brownmun.core.staff.StaffService;

public class StaffOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser>
{
    private static final Logger log = LoggerFactory.getLogger(StaffOAuth2UserService.class);

    private final OidcUserService base = new OidcUserService();
    private final StaffService staff;

    public StaffOAuth2UserService(StaffService staff)
    {
        this.staff = staff;
    }

    @Override
    public StaffUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException
    {
        OidcUser oidcUser = base.loadUser(userRequest);

        String email = oidcUser.getEmail();
        if (email == null)
        {
            OAuth2Error error = new OAuth2Error("missing_email", "Missing required \"email\" attribute", null);
            throw new OAuth2AuthenticationException(error, error.toString());
        }

        if (!staff.isStaffEmail(email))
        {
            log.warn("Non-staff login attempt by {}", email);
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED, "Not a staff email address: " + email,
                    null);
            throw new OAuth2AuthenticationException(error, error.toString());
        }

        log.info("Logging in staff member {}", email);

        // TODO: support non-staff staff and assigned committee IDs

        return new StaffUser(oidcUser, Set.of(), staff.isSecretariatEmail(email));
    }
}
