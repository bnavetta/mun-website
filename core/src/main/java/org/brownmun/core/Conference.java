package org.brownmun.core;

/**
 * Interface to represent the conference that this is.
 */
public interface Conference
{
    // TODO: maybe an enum?

    /**
     * The display name of the conference
     */
    String getName();

    /**
     * A key for lookups
     */
    String getKey();

    /**
     * The Google Apps email domain.
     */
    String getEmailDomain();
}
