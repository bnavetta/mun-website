package org.brownmun.core.print.model;

/**
 * Tracks the state of a printing request.
 */
public enum PrintRequestStatus
{
    /**
     * The request has not yet been claimed.
     */
    PENDING,

    /**
     * A staffer has claimed the request and is in the process of printing and
     * delivering it.
     */
    CLAIMED,

    /**
     * The print request has been delivered.
     */
    COMPLETED;
}
