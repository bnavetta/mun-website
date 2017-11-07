package org.brownmun.model.committee;

public enum  AwardType
{
    BEST_DELEGATE("Best Delegate"),
    OUTSTANDING_DELEGATE("Outstanding Delegate"),
    HONORABLE_DELEGATE("Honorable Delegate")
    ;

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
