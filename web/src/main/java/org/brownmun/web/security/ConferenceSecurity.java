package org.brownmun.web.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import org.brownmun.core.school.model.Advisor;

/**
 * Helpers for dealing with our Spring Security setup.
 */
public class ConferenceSecurity
{
    private ConferenceSecurity()
    {
    }

    /**
     * Makes the given advisor the current logged-in user.
     *
     * @param advisor an advisor
     */
    public static void authenticateAsAdvisor(Advisor advisor)
    {
        // See http://www.baeldung.com/manually-set-user-authentication-spring-security

        // This goes a bit more into Spring Security internals than I'd really like, but
        // seems like a bit of a necessary
        // evil. Basically, it creates a new Authentication object backed by an
        // AdvisorUser and stores that in the current
        // SecurityContext. Then, because there's a Spring Security filter to keep track
        // of the current user in each
        // session, we have to tell that about our updated context by storing it in the
        // session via RequestAttributes.
        // I'm not *entirely* sure that part is necessary.

        User user = new AdvisorUser(advisor);
        // sorta subtle, but we *must* use the 3-argument
        // UsernamePasswordAuthenticationToken constructor (the one that
        // takes a list of authorities), because that's the one that marks it as trusted
        Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);

        RequestAttributes req = RequestContextHolder.currentRequestAttributes();
        req.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context,
                RequestAttributes.SCOPE_SESSION);
    }
}
