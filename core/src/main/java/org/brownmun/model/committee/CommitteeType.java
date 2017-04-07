package org.brownmun.model.committee;

import org.apache.commons.lang3.text.WordUtils;

/**
 * A type of MUN committee
 */
public enum CommitteeType
{
	GENERAL,
	CRISIS,
	SPECIALIZED
	;

	@Override
	public String toString()
	{
		return WordUtils.capitalizeFully(name());
	}
}
