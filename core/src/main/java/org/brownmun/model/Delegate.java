package org.brownmun.model;

import com.google.common.base.MoreObjects;
import org.brownmun.model.committee.Position;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Delegate
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Allow null so school advisors can fill in delegate names once allocated
	@Size(min = 0)
	private String name;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "delegate")
	private Position position;

	@Transient
	public boolean isAssigned()
	{
		return getPosition() != null;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	private School school;

	public Long getId()
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

	public Position getPosition()
	{
		return position;
	}

	public void setPosition(Position position)
	{
		this.position = position;
	}

	public School getSchool()
	{
		return school;
	}

	public void setSchool(School school)
	{
		this.school = school;
	}



	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
			.add("id", id)
			.add("name", name)
			.add("position_id", position.getId())
			.add("school_id", school.getId())
			.toString();
	}
}
