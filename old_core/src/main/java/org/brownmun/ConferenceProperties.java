package org.brownmun;

import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Objects;

@ConfigurationProperties("staff")
public class ConferenceProperties
{
    private String name;

    private String infoEmail;

    private boolean decisionsPublic;

    private String logoUrl;

    private List<Event> events;

    private String facebookUrl;
    private String twitterUrl;
    private String instagramUrl;

    public String getName()
    {
        return name;
    }

    public boolean isDecisionsPublic()
    {
        return decisionsPublic;
    }

    public String getInfoEmail()
    {
        return infoEmail;
    }

    public void setInfoEmail(String infoEmail)
    {
        this.infoEmail = infoEmail;
    }

    public String getLogoUrl()
    {
        return logoUrl;
    }

    public List<Event> getEvents()
    {
        return events;
    }

    public String getFacebookUrl()
    {
        return facebookUrl;
    }

    public String getTwitterUrl()
    {
        return twitterUrl;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDecisionsPublic(boolean decisionsPublic)
    {
        this.decisionsPublic = decisionsPublic;
    }

    public void setLogoUrl(String logoUrl)
    {
        this.logoUrl = logoUrl;
    }

    public void setEvents(List<Event> events)
    {
        this.events = events;
    }

    public void setFacebookUrl(String facebookUrl)
    {
        this.facebookUrl = facebookUrl;
    }

    public void setTwitterUrl(String twitterUrl)
    {
        this.twitterUrl = twitterUrl;
    }

    public String getInstagramUrl()
    {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl)
    {
        this.instagramUrl = instagramUrl;
    }

    public boolean isBusun()
    {
        return "BUSUN".equals(name);
    }

    public boolean isBucs()
    {
        return "BUCS".equals(name);
    }

    public String getCommitteeEmail(String shortName)
    {
        if (isBucs())
        {
            return shortName + "@browncrisis.org";
        }
        else
        {
            return shortName + "@busun.org";
        }
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("infoEmail", infoEmail)
                .add("decisionsPublic", decisionsPublic)
                .add("logoUrl", logoUrl)
                .add("events", events)
                .add("facebookUrl", facebookUrl)
                .add("twitterUrl", twitterUrl)
                .add("instagramUrl", instagramUrl)
                .toString();
    }

    public static final class Event
    {
        private String date;
        private String description;

        public String getDate()
        {
            return date;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDate(String date)
        {
            this.date = date;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Event that = (Event) o;
            return Objects.equals(date, that.date) &&
                    Objects.equals(description, that.description);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(date, description);
        }

        @Override
        public String toString()
        {
            return MoreObjects.toStringHelper(this)
                    .add("date", date)
                    .add("description", description)
                    .toString();
        }
    }
}
