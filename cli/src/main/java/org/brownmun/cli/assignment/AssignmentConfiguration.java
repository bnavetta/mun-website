package org.brownmun.cli.assignment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import javax.persistence.EntityManager;

@Configuration
public class AssignmentConfiguration
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
}
