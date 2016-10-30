package org.brownmun.model;

import com.google.common.base.MoreObjects;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
public class Delegate
{
	@Setter(AccessLevel.NONE)
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
//	@SequenceGenerator(name = "delegate_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "delegate")
	private Position position;

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
