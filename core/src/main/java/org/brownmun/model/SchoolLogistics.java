package org.brownmun.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Embeddable entity to organize school logistical info
 */
@Embeddable
@Data
public class SchoolLogistics
{
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hotel_id")
	private Hotel hotel;

	@Setter(AccessLevel.NONE)
	@Formula("(hotel_id is not null)")
	private boolean usingHotel;

	private Boolean commuting;

	private Boolean usingShuttle;
}
