package org.brownmun.bucs;

import org.brownmun.core.Conference;

public class BucsConference implements Conference
{
    @Override
    public String getName()
    {
        return "BUCS";
    }

    @Override
    public String getKey()
    {
        return "bucs";
    }

    @Override
    public String getDomainName()
    {
        return "browncrisis.org";
    }
}
