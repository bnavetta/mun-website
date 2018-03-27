package org.brownmun.core.school;

import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.School;
import org.brownmun.core.school.model.SchoolApplication;

import java.util.Optional;

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
     * Find an advisor by email address
     * @param email the advisor's email address
     * @return the advisor, if found
     */
    Optional<Advisor> findAdvisor(String email);
}
