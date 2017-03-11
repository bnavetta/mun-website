package org.brownmun.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Token for adding a new school advisor
 */
@Entity
@Data
public class NewAdvisorToken
{
    /**
     * Randomly-generated token sent in the creation email
     */
    @Id
    private String token;

    /**
     * The school the advisor is part of
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    /**
     * Name of advisor being created
     */
    private String advisorName;

    /**
     * Email address of advisor being created
     */
    private String advisorEmail;
}
