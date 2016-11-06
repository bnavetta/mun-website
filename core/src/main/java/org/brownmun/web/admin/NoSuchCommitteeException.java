package org.brownmun.web.admin;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchCommitteeException extends RuntimeException
{
	@Getter
	private final Long id;

	public NoSuchCommitteeException(Long id)
	{
		super("No committee with id " + id + " exists");
		this.id = id;
	}
}
