package org.brownmun.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.brownmun.model.advisor.Advisor;
import org.brownmun.model.committee.Position;
import org.brownmun.model.delegation.SchoolInfo;
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

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	@Valid
	private SchoolInfo info;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school", cascade = CascadeType.ALL)
	private List<Delegate> delegates;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school", cascade = CascadeType.ALL)
    private List<Advisor> advisors;

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

	public static class View {
		public interface Summary {}
	}

	@Transient
	public boolean isWaitlisted()
    {
        return RegistrationStatus.WAITLISTED.equals(getStatus());
    }

    @Transient
    public boolean isAccepted()
    {
        return RegistrationStatus.ACCEPTED.equals(getStatus());
    }

    @Transient
    public boolean isRegistered()
    {
        return RegistrationStatus.REGISTERED.equals(getStatus());
    }

    @Transient
    public boolean isDenied()
    {
        return RegistrationStatus.DENIED.equals(getStatus());
    }

    @Transient
    public boolean isDropped()
    {
        return RegistrationStatus.DROPPED.equals(getStatus());
    }
}
