package org.brownmun.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
	@JsonView(View.Summary.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonView(View.Summary.class)
	@NotBlank
	private String name;

	@JsonView(View.Summary.class)
	@NotNull
	@Enumerated(EnumType.STRING)
	private RegistrationStatus status;

	@JsonView(View.Summary.class)
	@Valid
	@Embedded
	private Address address;

	@JsonView(View.Summary.class)
	@NotBlank
	private String phoneNumber;

	@JsonView(View.Summary.class)
	private Integer numberOfYearsAttended;

	@JsonView(View.Summary.class)
	private String yearsAttended;

	@Valid
	@Embedded
	private SchoolLogistics logistics;

	private String aboutText;

	@JsonView(View.Summary.class)
	private Integer requestedDelegates;

	private Integer requestedChaperones;

	@JsonView(View.Summary.class)
	@NotNull
	@Past
	private Instant registrationTime;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school", cascade = CascadeType.ALL)
	private List<Delegate> delegates;

	@Transient
	public List<Delegate> getAssignedDelegates()
	{
		return delegates.stream().filter(Delegate::isAssigned).collect(Collectors.toList());
	}

	@Transient
	public long getUnassignedDelegateCount()
	{
		return delegates.stream().filter(d -> !d.isAssigned()).count();
	}

	public boolean hasPosition(Position position)
	{
		return position.isAssigned() && position.getDelegate().getSchool().getId() == this.getId();
	}

	public Date getRegistrationTimeAsDate()
	{
		return Date.from(registrationTime);
	}

	public static class View {
		public interface Summary {}
	}
}
