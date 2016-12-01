package org.brownmun.web.admin;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchSchoolException extends RuntimeException
{
	@Getter
	private final Long id;

	public NoSuchSchoolException(Long id)
	{
		super("No school with id " + id + " exists");
		this.id = id;
	}
}
