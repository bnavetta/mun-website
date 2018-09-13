package org.brownmun.web.advisors;

import org.brownmun.core.school.AdvisorService;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.web.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import java.io.IOException;


/**
 * Marks when an advisor has logged into the site. This sounds a bit weird, but it's just so we
 * know when to check their supplemental info form, etc.
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class AdvisorTrackingFilter extends GenericFilterBean
{
    private AdvisorService advisorService;

    @Autowired
    public void setAdvisorService(AdvisorService advisorService)
    {
        this.advisorService = advisorService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        try
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.getPrincipal() instanceof User)
            {
                User user = (User) auth.getPrincipal();

                if (user.isAdvisor())
                {
                    Advisor advisor = user.asAdvisor();
                    logger.debug("Observing advisor " + advisor);
                    advisorService.markSeen(advisor);
                }
            }
        }
        finally
        {
            chain.doFilter(request, response);
        }
    }
}
