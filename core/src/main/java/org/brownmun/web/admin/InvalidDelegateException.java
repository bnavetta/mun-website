package org.brownmun.web.admin;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when trying to do something to a school's delegate but the delegate id was invalid.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDelegateException extends RuntimeException
{
	@Getter
	private final Long id;

	public InvalidDelegateException(Long id)
	{
		super("Invalid delegate id " + id);
		this.id = id;
	}
}

