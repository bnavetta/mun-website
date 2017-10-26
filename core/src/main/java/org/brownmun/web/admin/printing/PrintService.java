package org.brownmun.web.admin.printing;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import com.google.common.io.ByteStreams;
import org.brownmun.model.PrintRequest;
import org.brownmun.model.repo.PrintingRepository;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

/**
 * Service for managing print requests
 */
@Service
public class PrintService
{
    private static final Logger log = LoggerFactory.getLogger(PrintService.class);

    private static final Escaper QUOTE_ESCAPER = Escapers.builder()
            .addEscape('"', "_")
            .addEscape('\'', "_")
            .build();

    private final PrintingRepository printRepo;
    private final EntityManager em; // needed to create BLOBs

    @Autowired
    public PrintService(PrintingRepository printRepo, EntityManager em)
    {
        this.printRepo = printRepo;
        this.em = em;
    }

    /**
     * Write the file contents for a print request to an HTTP response. This somewhat breaks encapsulation, but
     * the content has to be read within a database transaction, and it's a lot easier to do it this way than to
     * return some kind of custom resource representation that can open a transaction.
     * @param id the print request ID
     * @param response the HTTP response to write to
     */
    @Transactional
    public void writeData(long id, HttpServletResponse response) throws IOException
    {
        PrintRequest request = printRepo.findOne(id);
        if (request == null)
        {
            response.sendError(404, "Print request " + id + " not found");
        }
        else
        {
            try
            {
                Blob data = request.getData();
                response.setStatus(200);
                String contentDisposition = String.format("attachment; filename=\"%s\"",
                        QUOTE_ESCAPER.escape(request.getFilename()));
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
                response.setContentLengthLong(data.length());
                response.setContentType(request.getContentType());
                response.flushBuffer();
                ServletOutputStream out = response.getOutputStream();
                ByteStreams.copy(data.getBinaryStream(), out);
                out.flush();
                data.free();
            }
            catch (SQLException e)
            {
                log.error("Error serving print request content", e);
                response.sendError(500, "Error serving file content");
            }
        }
    }

    @Transactional
    public PrintRequest createRequest(PrintSubmission submission) throws IOException
    {
        PrintRequest req = new PrintRequest();
        req.setDeliveryLocation(submission.getDeliveryLocation());
        req.setFilename(submission.getFile().getOriginalFilename());
        req.setContentType(submission.getFile().getContentType());
        req.setNumCopies(submission.getNumCopies());
        req.setRequester(submission.getRequester());
        req.setStatus(PrintRequest.Status.PENDING);

        LobHelper lobHelper = em.unwrap(Session.class).getLobHelper();
        Blob blob = lobHelper.createBlob(submission.getFile().getInputStream(), submission.getFile().getSize());
        req.setData(blob);

        req = printRepo.save(req);
        log.debug("Created print request {}", req);
        return req;
    }

    @Transactional
    public List<PrintRequest> getOpenRequests()
    {
        return printRepo.findOpenRequests();
    }

    @Transactional
    public void setRequestStatus(long id, PrintRequest.Status status)
    {
        log.debug("Marking print request {} as {}", id, status);
        printRepo.setStatus(id, status);
    }
}
