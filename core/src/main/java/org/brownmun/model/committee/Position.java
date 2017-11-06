package org.brownmun.model.committee;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import org.brownmun.model.Attendance;
import org.brownmun.model.Delegate;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A specific position on a committee, which may be assigned to delegates.
 */
@Entity
public class Position implements Comparable<Position>
{
	@JsonView(AssignmentView.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@JsonView(AssignmentView.class)
	@NotBlank
	private String name;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "committee_id")
	private Committee committee;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delegate_id")
	private Delegate delegate;

	@Embedded
	private Attendance attendance = new Attendance();

	@Formula("(delegate_id is not null)")
	private boolean assigned;

	public long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Committee getCommittee()
	{
		return committee;
	}

	public void setCommittee(Committee committee)
	{
		this.committee = committee;
	}

	public Delegate getDelegate()
	{
		return delegate;
	}

	public void setDelegate(Delegate delegate)
	{
		this.delegate = delegate;
	}

	public boolean isAssigned()
	{
		return assigned;
	}

	public void setAssigned(boolean assigned)
	{
		this.assigned = assigned;
	}

    public Attendance getAttendance()
    {
        return attendance;
    }

    public void setAttendance(Attendance attendance)
    {
        this.attendance = attendance;
    }

    @Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return id == position.id &&
				assigned == position.assigned &&
				Objects.equals(name, position.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name, assigned);
	}

	@Override
	public String toString()
	{
		// Use ids to prevent infinite toString recursion
		return MoreObjects.toStringHelper(this)
			.add("id", id)
			.add("name", name)
			.add("committee_id", committee.getId())
			.add("delegate_id", delegate != null ? delegate.getId() : "null")
			.add("assigned", assigned)
            .add("attendance", attendance)
			.toString();
	}

	@Override
	public int compareTo(Position o)
	{
		return ComparisonChain.start()
			.compare(this.committee, o.committee)
			.compare(this.name, o.name)
			.result();
	}

	public static class AssignmentView {}
}
