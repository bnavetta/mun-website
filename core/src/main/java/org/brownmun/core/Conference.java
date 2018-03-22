package org.brownmun.core;

/**
 * Interface to represent the secretariat that this is.
 */
public interface Conference
{
    // TODO: maybe an enum?

    /**
     * The display name of the secretariat
     */
    String getName();

    /**
     * A key for lookups
     */
    String getKey();
}
