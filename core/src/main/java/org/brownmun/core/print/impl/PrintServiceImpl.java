package org.brownmun.core.print.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import org.brownmun.core.print.PrintService;
import org.brownmun.core.print.model.PrintRequest;
import org.brownmun.core.print.model.PrintRequestStatus;

/**
 * Print queue implementation.
 */
@Service
public class PrintServiceImpl implements PrintService
{
    private static final Logger log = LoggerFactory.getLogger(PrintServiceImpl.class);

    private final PrintRequestRepository repo;
    private final TransactionTemplate txTemplate;

    private final ReadWriteLock subscribersLock;
    private final Map<Object, Consumer<PrintRequest>> subscribers;

    public PrintServiceImpl(PrintRequestRepository repo, TransactionTemplate txTemplate)
    {
        this.repo = repo;
        this.txTemplate = txTemplate;
        this.subscribers = new HashMap<>();
        this.subscribersLock = new ReentrantReadWriteLock();
    }

    @Override
    public Optional<PrintRequest> getRequest(long id)
    {
        return repo.findById(id);
    }

    @Override
    public PrintRequest submitRequest(int numCopies, String deliveryLocation, String requester, String filename,
            String contentType, byte[] source)
    {
        PrintRequest saved = txTemplate.execute(tx -> {
            PrintRequest request = new PrintRequest();
            request.setStatus(PrintRequestStatus.PENDING);
            request.setNumCopies(numCopies);
            request.setDeliveryLocation(deliveryLocation);
            request.setRequester(requester);
            request.setFilename(filename);
            request.setContentType(contentType);
            request.setData(source);
            request.setSubmissionTime(Instant.now());
            return repo.save(request);
        });

        log.debug("{} submitted a print request for {} copies of {}", requester, numCopies, filename);
        broadcast(saved);
        return saved;
    }

    @Override
    public PrintRequest claimRequest(long id)
    {
        PrintRequest request = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Print request not found"));

        if (request.getStatus() != PrintRequestStatus.PENDING)
        {
            throw new IllegalStateException("Cannot claim print request in this state");
        }

        request.setStatus(PrintRequestStatus.CLAIMED);
        PrintRequest saved = txTemplate.execute(tx -> repo.save(request));
        broadcast(saved);
        log.debug("Marked {} as claimed", id);
        return saved;
    }

    @Override
    public PrintRequest completeRequest(long id)
    {
        PrintRequest request = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Print request not found"));

        if (request.getStatus() != PrintRequestStatus.CLAIMED)
        {
            throw new IllegalStateException("Cannot complete print request in this state");
        }

        request.setStatus(PrintRequestStatus.COMPLETED);
        PrintRequest saved = txTemplate.execute(tx -> repo.save(request));
        broadcast(saved);
        log.debug("Marked {} as completed", id);
        return saved;
    }

    @Override
    public Object addSubscriber(Consumer<PrintRequest> subscriber)
    {
        Object key = new Object();
        try
        {
            subscribersLock.writeLock().lock();
            subscribers.put(key, subscriber);
        }
        finally
        {
            subscribersLock.writeLock().unlock();
        }

        txTemplate.execute(tx -> {
            repo.findAll().forEach(subscriber);
            return null;
        });

        return key;
    }

    @Override
    public void removeSubscriber(Object key)
    {
        try
        {
            subscribersLock.writeLock().lock();
            subscribers.remove(key);
        }
        finally
        {
            subscribersLock.writeLock().unlock();
        }
    }

    private void broadcast(PrintRequest request)
    {
        try
        {
            subscribersLock.readLock().lock();

            for (var subscriber : subscribers.values())
            {
                subscriber.accept(request);
            }
        }
        finally
        {
            subscribersLock.readLock().unlock();
        }
    }
}
