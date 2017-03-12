package org.brownmun.web.security;

import org.brownmun.model.Advisor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link AccessDeniedHandler} that can redirect between staff and advisor pages
 */
public class StaffAwareAccessDeniedHandler extends AccessDeniedHandlerImpl
{
    private final UrlPathHelper pathHelper = new UrlPathHelper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
        {
            super.handle(request, response, accessDeniedException);
            return;
        }

        Object principal = authentication.getPrincipal();
        if (principal == null)
        {
            super.handle(request, response, accessDeniedException);
            return;
        }

        String path = pathHelper.getPathWithinApplication(request);

        // Staff going to /yourbusun
        if (path.startsWith("/yourbusun") && !(principal instanceof Advisor))
        {
            logger.info("Redirecting advisor from admin page to advisor page");
            request.getRequestDispatcher("/admin").forward(request, response);
        }
        else if (path.startsWith("/admin") && principal instanceof Advisor)
        {
            logger.info("Redirecting staff member from advisor page to admin page");
            request.getRequestDispatcher("/yourbusun").forward(request, response);
        }
        else
        {
            super.handle(request, response, accessDeniedException);
        }
    }
}
