package org.brownmun.db.committee;

/**
 * A kind of award. Usually, a committee will have one best delegate, 1-2
 * outstanding delegates, and 0-3 honorable delegates.
 */
public enum AwardType
{
    BEST_DELEGATE("Best Delegate"), OUTSTANDING_DELEGATE("Outstanding Delegate"), HONORABLE_DELEGATE(
            "Honorable Delegate");

    private final String displayName;

    AwardType(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }
}
