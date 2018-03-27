package org.brownmun.core.school.model;

import javax.persistence.*;

/**
 * A school's application form.
 */
@Entity
@Table(name = "school_application")
public class Application
{
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private School school;
}
