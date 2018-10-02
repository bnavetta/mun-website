package org.brownmun.cli.positions;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

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
    public Allocator allocator(EntityManager em)
    {
        return new Allocator(new CsvMapper(), em);
    }

    @Bean
    public Assigner assigner(SchoolService schoolService, CommitteeService committeeService, ObjectMapper jsonMapper,
            TransactionTemplate tx)
    {
        return new Assigner(jsonMapper, new CsvMapper(), committeeService, schoolService, tx);
    }
}
