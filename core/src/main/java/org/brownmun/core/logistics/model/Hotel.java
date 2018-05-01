package org.brownmun.core.logistics.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * A partner hotel. We store these in the database so that the hotels page and
 * supplemental info forms can use the same backing list and so that we can
 * easily query schools at each hotel.
 */
@Entity
public class Hotel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer rate;

    private String address;

    private String phone;

    private String notes;

    /**
     * Creates a {@link Hotel} with only an ID, for deserializing JSON form responses
     */
    public Hotel(long id)
    {
        this.id = id;
    }

    /**
     * Default constructor for Hibernate
     */
    public Hotel()
    {

    }

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

    public Integer getRate()
    {
        return rate;
    }

    public void setRate(Integer rate)
    {
        this.rate = rate;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(name, hotel.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("rate", rate)
                .add("address", address).add("phone", phone).add("notes", notes).toString();
    }
}
