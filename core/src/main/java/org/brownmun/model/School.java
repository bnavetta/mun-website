package org.brownmun.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import javax.persistence.*;

/**
 * A school applying to BUCS/BUSUN
 */
@Data
@Entity
public class School
{
	@Setter(AccessLevel.NONE)
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
//	@SequenceGenerator(name = "school_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school", cascade = CascadeType.ALL)
	private List<Delegate> delegates;
}
