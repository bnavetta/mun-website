package org.brownmun.util;

/**
 * Helper utilities for formatting data, usually for data export.
 */
public class Formatting
{
    private Formatting() {}

    /**
     * Format a boolean value as "Yes"/"No"
     * @param b the value
     * @return a yes/no string
     */
    public static String yesNo(boolean b)
    {
        return b ? "Yes" : "No";
    }
}
