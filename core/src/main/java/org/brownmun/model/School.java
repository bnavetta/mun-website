package org.brownmun.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.time.Instant;
import java.util.List;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

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

	@NotBlank
	private String name;

	@NotNull
	@Enumerated(EnumType.STRING)
	private RegistrationStatus status;

	@Valid
	@Embedded
	private Address address;

	@NotBlank
	private String phoneNumber;

	private Integer numberOfYearsAttended;
	private String yearsAttended;

	@Valid
	@Embedded
	private SchoolLogistics logistics;

	private String aboutText;

	@NotNull
	@Past
	private Instant registrationTime;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school", cascade = CascadeType.ALL)
	private List<Delegate> delegates;
}
