package org.brownmun.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.MoreObjects;
import org.brownmun.model.advisor.Advisor;
import org.brownmun.model.committee.Position;
import org.brownmun.model.delegation.SchoolInfo;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A school applying to BUCS/BUSUN
 */
@Entity
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

    public RegistrationStatus getStatus()
    {
        return status;
    }

    public void setStatus(RegistrationStatus status)
    {
        this.status = status;
    }

    public SchoolInfo getInfo()
    {
        return info;
    }

    public void setInfo(SchoolInfo info)
    {
        this.info = info;
    }

    public List<Delegate> getDelegates()
    {
        return delegates;
    }

    public void setDelegates(List<Delegate> delegates)
    {
        this.delegates = delegates;
    }

    public List<Advisor> getAdvisors()
    {
        return advisors;
    }

    public void setAdvisors(List<Advisor> advisors)
    {
        this.advisors = advisors;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return Objects.equals(id, school.id) &&
                Objects.equals(name, school.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("status", status)
                .add("info", info)
                .add("delegates", delegates)
                .add("advisors", advisors)
                .toString();
    }

    public static class View {
        public interface Summary {}
    }
}
