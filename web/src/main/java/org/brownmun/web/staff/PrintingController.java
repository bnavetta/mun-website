package org.brownmun.web.staff;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import org.brownmun.core.print.PrintService;
import org.brownmun.core.print.model.PrintRequest;
import org.brownmun.web.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/staff/print-system")
@PreAuthorize("hasRole('STAFF')")
public class PrintingController
{
    private static final Escaper QUOTE_ESCAPER = Escapers.builder()
            .addEscape('"', "_")
            .addEscape('\'', "_")
            .addEscape(';', "_")
            .build();

    private static final Logger log = LoggerFactory.getLogger(PrintingController.class);
    private final PrintService printService;

    public PrintingController(PrintService printService)
    {
        this.printService = printService;
    }

    @GetMapping("/queue")
    public ResponseEntity<SseEmitter> queue()
    {
        SseEmitter out = new SseEmitter();
        Object registration = printService.addSubscriber(request -> {
            try {
                out.send(SseEmitter.event()
                .name("print-update")
                .data(request, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                log.warn("Error sending print queue update", e);
            }
        });

        out.onCompletion(() -> printService.removeSubscriber(registration));
        out.onError(err -> printService.removeSubscriber(registration));
        out.onTimeout(() -> printService.removeSubscriber(registration));

        return ResponseEntity.ok(out);
    }

    @PostMapping("/submit")
    public ResponseEntity<PrintRequest> submitPrintRequest(@AuthenticationPrincipal User user, @RequestParam MultipartFile source, @RequestParam int numCopies, @RequestParam String deliveryLocation)
    {
        String requester = user.getUsername();
        try
        {
            PrintRequest request = printService.submitRequest(numCopies, deliveryLocation, requester, source.getOriginalFilename(), source.getContentType(), source.getBytes());
            return ResponseEntity.ok(request);
        }
        catch (IOException e)
        {
            log.error("Unable to submit print request for {}: {}", source.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/claim/{id}")
    @PreAuthorize("hasRole('SECRETARIAT')")
    public ResponseEntity<PrintRequest> claimRequest(@PathVariable long id)
    {
        try
        {
            return ResponseEntity.ok(printService.claimRequest(id));
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/complete/{id}")
    @PreAuthorize("hasRole('SECRETARIAT')")
    public ResponseEntity<PrintRequest> completeRequest(@PathVariable long id)
    {
        try
        {
            return ResponseEntity.ok(printService.completeRequest(id));
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable long id)
    {
        return printService.getRequest(id).map(request -> {
            String contentDisposition = "attachment; filename=\"" + QUOTE_ESCAPER.escape(request.getFilename()) + "\"";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(request.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(request.getData());
        }).orElse(ResponseEntity.notFound().build());
    }
}
