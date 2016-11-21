package org.brownmun.model;

import com.google.common.base.MoreObjects;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
public class Delegate
{
	@Setter(AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Allow null so school advisors can fill in delegate names once allocated
	@Size(min = 0)
	private String name;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "delegate")
	private Position position;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	private School school;

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
