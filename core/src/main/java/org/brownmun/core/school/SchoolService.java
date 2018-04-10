package org.brownmun.core.school;

import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.School;
import org.brownmun.core.school.model.SchoolApplication;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public interface SchoolService
{
    /**
     * Register a new school
     *
     * @param name the school's name
     * @param advisorName the school advisor's name
     * @param advisorEmail the school advisor's email address
     * @param advisorPassword the school advisor's password
     * @param advisorPhoneNumber the school advisor's phone number
     * @return the newly-persisted school
     */
    School registerSchool(String name, String advisorName, String advisorEmail, String advisorPassword, String advisorPhoneNumber);

    /**
     * Save a school's application.
     * @param school the full school, with application contents
     */
    void submitApplication(School school);

    /**
     * Update a school's application.
     * @param advisor the advisor making the update
     * @param application the revised application
     */
    void updateApplication(Advisor advisor, SchoolApplication application);

    /**
     * Find an advisor by email address
     * @param email the advisor's email address
     * @return the advisor, if found
     */
    Optional<Advisor> findAdvisor(String email);

    /**
     * Determine the ID of the school a certain delegate is part of.
     * @param delegateId the delegate's ID
     * @return the school ID, if found
     */
    OptionalLong findSchoolId(long delegateId);

    /**
     * Force-resolve the possibly-proxied {@link School} associated with an {@link Advisor}.
     * @param advisor an advisor
     * @return a non-proxied {@link School}, which can be safely serialized as JSON, etc.
     */
    School loadSchool(Advisor advisor);

    List<School> listSchools();
}
