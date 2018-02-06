package org.brownmun.model.repo;

import org.brownmun.model.PrintRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Blob;
import java.util.List;
import java.util.Optional;

public interface PrintingRepository extends JpaRepository<PrintRequest, Long>
{
    /**
     * Fetch all non-completed print requests.
     * @return all open print requests, ordered by submission time
     */
    @Query("SELECT p FROM PrintRequest p WHERE p.status <> org.brownmun.model.PrintRequest$Status.COMPLETED ORDER BY p.submissionTime ASC")
    List<PrintRequest> findOpenRequests();

    @Modifying
    @Query("UPDATE PrintRequest SET status = ?2 WHERE id = ?1")
    void setStatus(long id, PrintRequest.Status status);
}
