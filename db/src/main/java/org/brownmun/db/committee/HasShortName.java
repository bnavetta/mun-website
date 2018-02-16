package org.brownmun.db.committee;

/**
 * Marker for objects with short names. Short names are human-readable
 * identifiers for committees and joint crises. They're used as the account
 * names for email addresses and as filenames for external content, like
 * background guides.
 */
public interface HasShortName
{
    /**
     * Get this object's short name.
     */
    String getShortName();
}
