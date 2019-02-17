package org.brownmun.core.print.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.brownmun.core.print.model.PrintRequest;

/**
 * Access to the {@link PrintRequest} database table.
 */
public interface PrintRequestRepository extends JpaRepository<PrintRequest, Long>
{
    @Query("SELECT p FROM PrintRequest p WHERE p.status <> org.brownmun.core.print.model.PrintRequestStatus.COMPLETED")
    List<PrintRequest> findAllInQueue();
}
