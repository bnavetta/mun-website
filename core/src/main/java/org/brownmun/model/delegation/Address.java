package org.brownmun.model.delegation;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.brownmun.model.School;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Embeddable
public class Address
{
	@JsonView(School.View.Summary.class)
	@NotBlank
	private String country;

	@JsonView(School.View.Summary.class)
	@NotBlank
	private String streetAddress;

	@JsonView(School.View.Summary.class)
	@NotBlank
	private String city;

	@JsonView(School.View.Summary.class)
	@Size(min = 0)
	private String state;

	@JsonView(School.View.Summary.class)
	@Size(min = 0)
	private String postalCode;

	@Override
	public String toString()
	{
		return String.format("%s, %s %s %s - %s", streetAddress, city, state, postalCode, country);
	}
}
