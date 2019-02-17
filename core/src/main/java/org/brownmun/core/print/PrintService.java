package org.brownmun.core.print;

import java.util.Optional;
import java.util.function.Consumer;

import org.brownmun.core.print.model.PrintRequest;

/**
 * Service layer for managing the print queue.
 */
public interface PrintService
{
    Optional<PrintRequest> getRequest(long id);

    /**
     * Submit a new print request
     *
     * @param numCopies the number of copies of the document to print
     * @param deliveryLocation where to deliver the printed papers
     * @param requester who requested the print
     * @param filename the name of the document being printed
     * @param contentType the content type of the document
     * @param source the document to print
     * @return the submitted request
     */
    PrintRequest submitRequest(int numCopies, String deliveryLocation, String requester, String filename,
            String contentType, byte[] source);

    /**
     * Mark a print request as claimed
     *
     * @param id the ID of the print request
     */
    PrintRequest claimRequest(long id);

    /**
     * Mark a print request as completed.
     *
     * @param id the ID of the print request
     */
    PrintRequest completeRequest(long id);

    /**
     * Register a subscriber for print queue changes.
     *
     * @param subscriber a function to call on every updated print request
     * @return a key which can be used to remove the subscriber
     */
    Object addSubscriber(Consumer<PrintRequest> subscriber);

    /**
     * Unregister a print queue subscriber.
     *
     * @param key the registration key from {@link #addSubscriber(Consumer)}
     */
    void removeSubscriber(Object key);
}
