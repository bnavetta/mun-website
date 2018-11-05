package org.brownmun.core.school.model;

import javax.persistence.*;

import com.google.common.base.MoreObjects;

import org.brownmun.core.committee.model.Position;

/**
 * Links a {@link School} to a {@link Position} and stores information about the
 * actual student.
 *
 * This can't quite just be a foreign key column on {@link Position} or a
 * JPA-managed join table because of the extra information needed about the
 * student.
 */
@Entity
public class Delegate
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The delegate's name (the actual student).
     */
    private String name;

    /**
     * The delegate's attendance record (sessions they were present at)
     */
    @Embedded
    private Attendance attendance = new Attendance();

    /**
     * The school this delegate is from.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    /**
     * The position this delegate is representing.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Attendance getAttendance()
    {
        return attendance;
    }

    public void setAttendance(Attendance attendance)
    {
        this.attendance = attendance;
    }

    public School getSchool()
    {
        return school;
    }

    public void setSchool(School school)
    {
        this.school = school;
    }

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("schooId", school.getId())
                .toString();
    }

}
