package org.brownmun.core.print.model;

import java.sql.Blob;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

/**
 * A printing request. Submitted by committees and fulfilled by staffers, who
 * also deliver the printed files in person.
 */
@Entity
public class PrintRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    private int numCopies;

    @NotBlank
    private String deliveryLocation;

    @Past
    private Instant submissionTime;

    @Email
    @NotBlank
    private String requester;

    @NotBlank
    private String filename;

    private String contentType;

    @Lob
    @JsonIgnore
    private Blob data;

    @Type(type = "org.brownmun.core.db.PostgresEnumType", parameters = @org.hibernate.annotations.Parameter(name = "postgres_enum", value = "print_request_status"))
    @Enumerated(EnumType.STRING)
    private Status status;

    @PrePersist
    protected void setSubmissionTime()
    {
        this.submissionTime = Instant.now();
    }

    public Long getId()
    {
        return id;
    }

    public int getNumCopies()
    {
        return numCopies;
    }

    public void setNumCopies(int numCopies)
    {
        this.numCopies = numCopies;
    }

    public String getDeliveryLocation()
    {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation)
    {
        this.deliveryLocation = deliveryLocation;
    }

    public Instant getSubmissionTime()
    {
        return submissionTime;
    }

    public String getRequester()
    {
        return requester;
    }

    public void setRequester(String requester)
    {
        this.requester = requester;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public Blob getData()
    {
        return data;
    }

    public void setData(Blob data)
    {
        this.data = data;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PrintRequest that = (PrintRequest) o;
        return numCopies == that.numCopies && Objects.equals(deliveryLocation, that.deliveryLocation)
                && Objects.equals(submissionTime, that.submissionTime) && Objects.equals(requester, that.requester)
                && Objects.equals(filename, that.filename) && Objects.equals(status, that.status)
                && Objects.equals(contentType, that.contentType);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(numCopies, deliveryLocation, submissionTime, requester, filename, status, contentType);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("numCopies", numCopies)
                .add("deliveryLocation", deliveryLocation)
                .add("submissionTime", submissionTime)
                .add("requester", requester)
                .add("filename", filename)
                .add("contentType", contentType)
                .add("status", status)
                .toString();
    }

    /**
     * Tracks the state of a printing request.
     */
    public enum Status
    {
        /**
         * The request has not yet been claimed.
         */
        PENDING,

        /**
         * A staffer has claimed the request and is in the process of printing and
         * delivering it.
         */
        CLAIMED,

        /**
         * The print request has been delivered.
         */
        COMPLETED;
    }
}
