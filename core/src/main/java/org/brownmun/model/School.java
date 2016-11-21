package org.brownmun.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.time.Instant;
import java.util.List;
import javax.persistence.*;

/**
 * A school applying to BUCS/BUSUN
 */
@Data
@Entity
@EqualsAndHashCode(exclude = {"delegates"})
public class School
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private RegistrationStatus status;

	@Embedded
	private Address address;

	private String phoneNumber;

	private Integer numberOfYearsAttended;
	private String yearsAttended;

	@Embedded
	private SchoolLogistics logistics;

	private String aboutText;

	private Instant registrationTime;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school", cascade = CascadeType.ALL)
	private List<Delegate> delegates;
}
