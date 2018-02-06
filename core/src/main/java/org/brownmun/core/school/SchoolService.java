package org.brownmun.core.school;

import org.brownmun.db.school.Advisor;
import org.brownmun.db.school.School;

public interface SchoolService
{
    /**
     * Register a new school
     * @param initialAdvisor information about the advisor account to create for the school
     * @param name the school's name
     * @return the newly-persisted school
     */
    School registerSchool(Advisor initialAdvisor, String name);
}
