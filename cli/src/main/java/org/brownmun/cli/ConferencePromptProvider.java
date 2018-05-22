package org.brownmun.cli;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

import org.brownmun.core.Conference;

@Component
public class ConferencePromptProvider implements PromptProvider
{
    private final Conference conference;

    @Autowired
    public ConferencePromptProvider(Conference conference)
    {
        this.conference = conference;
    }

    @Override
    public AttributedString getPrompt()
    {
        return new AttributedString(conference.getName() + " $ ",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
    }
}
