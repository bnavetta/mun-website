package org.brownmun.web.support.sentry;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import org.brownmun.core.Conference;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.web.security.User;

import io.sentry.Sentry;
import io.sentry.event.UserBuilder;

/**
 * Adds extra context information to Sentry, like user info
 */
@Component
public class SentryDataSource implements ApplicationListener<InteractiveAuthenticationSuccessEvent>
{
    private static final Logger log = LoggerFactory.getLogger(SentryDataSource.class);

    private final Conference conference;

    @Autowired
    public SentryDataSource(Conference conference)
    {
        this.conference = conference;
    }

    @PostConstruct
    public void configureSentry()
    {
        log.info("Adding Sentry tags");
        Sentry.getContext().addTag("conference", conference.getKey());
    }

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event)
    {
        User user = (User) event.getAuthentication().getPrincipal();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.setEmail(user.getEmail());
        userBuilder.setUsername(user.getName());

        Map<String, Object> data = Maps.newHashMap();
        data.put("staff", user.isStaff());
        if (user.isAdvisor())
        {
            Advisor advisor = user.asAdvisor();
            data.put("schoolId", advisor.getSchoolId());
            data.put("phoneNumber", advisor.getPhoneNumber());
        }
        userBuilder.setData(data);
        io.sentry.event.User sentryUser = userBuilder.build();
        log.info("Associating user {} with Sentry", sentryUser);

        Sentry.getContext().setUser(sentryUser);
    }
}
