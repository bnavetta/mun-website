package org.brownmun.web.admin;

/**
 * All of a school's delegates are allocated
 */
public class NoFreeDelegatesException extends RuntimeException
{
	private final Long schoolId;

	public NoFreeDelegatesException(Long schoolId)
	{
		super("All delegates for school " + schoolId + " are allocated");
		this.schoolId = schoolId;
	}

	public Long getSchoolId()
	{
		return schoolId;
	}
}
