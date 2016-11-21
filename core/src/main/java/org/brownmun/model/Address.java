package org.brownmun.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Address
{
	private String country;
	private String streetAddress;
	private String city;
	private String state;
	private String postalCode;
}
