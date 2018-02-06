package org.brownmun.model;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Status of a school that applied.
 */
public enum RegistrationStatus
{
	REGISTERED,
	WAITLISTED,
	ACCEPTED,
	DENIED,
	DROPPED
	;

	@Override
	public String toString()
	{
		return WordUtils.capitalizeFully(name());
	}

	public String messageKey()
    {
        return name().toLowerCase();
    }
}
