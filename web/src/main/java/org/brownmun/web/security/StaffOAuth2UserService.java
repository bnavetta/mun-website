package org.brownmun.web.security;

import org.brownmun.core.staff.StaffService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Set;

public class StaffOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>
{
    private final DefaultOAuth2UserService base = new DefaultOAuth2UserService();
    private final StaffService staff;

    public StaffOAuth2UserService(StaffService staff)
    {
        this.staff = staff;
    }

    @Override
    public StaffUser loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
    {
        OAuth2User oAuth2User = base.loadUser(userRequest);

        String email = (String) oAuth2User.getAttributes().get("email");
        if (email == null)
        {
            OAuth2Error error = new OAuth2Error("missing_email", "Missing required \"email\" attribute", null);
            throw new OAuth2AuthenticationException(error, error.toString());
        }

        if (!staff.isStaffEmail(email))
        {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED, "Not a staff email address: " + email, null);
            throw new OAuth2AuthenticationException(error, error.toString());
        }

        String name = (String) oAuth2User.getAttributes().getOrDefault("name", email);

        // TODO: support non-staff staff and assigned committee IDs

        return new StaffUser(email, name, Set.of(), staff.isSecretariatEmail(email));
    }
}
