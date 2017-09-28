package org.brownmun.model.delegation;

import com.fasterxml.jackson.annotation.JsonView;
import org.brownmun.model.School;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.util.Objects;

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

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getStreetAddress()
	{
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress)
	{
		this.streetAddress = streetAddress;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getPostalCode()
	{
		return postalCode;
	}

	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address = (Address) o;
		return Objects.equals(country, address.country) &&
				Objects.equals(streetAddress, address.streetAddress) &&
				Objects.equals(city, address.city) &&
				Objects.equals(state, address.state) &&
				Objects.equals(postalCode, address.postalCode);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(country, streetAddress, city, state, postalCode);
	}
}
