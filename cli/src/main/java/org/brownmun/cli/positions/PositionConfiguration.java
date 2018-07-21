package org.brownmun.cli.positions;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import org.brownmun.cli.positions.allocation.Allocator;
import org.brownmun.cli.positions.assignment.Assigner;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.school.SchoolService;

@Configuration
public class PositionConfiguration
{
    @Bean
    public CsvMapper csvMapper()
    {
        return new CsvMapper();
    }

    @Bean
    public Allocator allocator(EntityManager em)
    {
        return new Allocator(csvMapper(), em);
    }

    @Bean
    public Assigner assigner(SchoolService schoolService, CommitteeService committeeService, ObjectMapper jsonMapper)
    {
        return new Assigner(jsonMapper, csvMapper(), committeeService, schoolService);
    }
}