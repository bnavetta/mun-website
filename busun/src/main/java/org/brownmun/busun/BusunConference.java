package org.brownmun.busun;

import org.brownmun.core.Conference;

public class BusunConference implements Conference
{
    @Override
    public String getName()
    {
        return "BUSUN";
    }

    @Override
    public String getKey()
    {
        return "busun";
    }

    @Override
    public String getDomainName()
    {
        return "busun.org";
    }
}
