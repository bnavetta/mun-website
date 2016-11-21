package org.brownmun.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Embeddable
public class Address
{
	@NotBlank
	private String country;

	@NotBlank
	private String streetAddress;

	@NotBlank
	private String city;

	@Size(min = 0)
	private String state;

	@Size(min = 0)
	private String postalCode;
}
