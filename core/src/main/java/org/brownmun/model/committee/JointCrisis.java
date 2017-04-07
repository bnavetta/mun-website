package org.brownmun.model.committee;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Collection;
import javax.persistence.*;

/**
 * Crisis simulations can consist of multiple committees.
 */
@Entity
@Data
public class JointCrisis
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "joint_crisis_committees",
        joinColumns = @JoinColumn(name = "crisis_id"),
        inverseJoinColumns = @JoinColumn(name = "committee_id")
    )
    private Collection<Committee> committees;
}
