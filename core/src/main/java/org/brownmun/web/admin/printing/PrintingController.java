package org.brownmun.web.admin.printing;

import org.brownmun.model.PrintRequest;
import org.brownmun.web.ApiResponse;
import org.brownmun.web.security.StaffMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Handles API endpoints and view serving for printing
 */
@Controller
@RequestMapping({"/admin/print", "/admin/print/"})
public class PrintingController
{
    private static final Logger log = LoggerFactory.getLogger(PrintingController.class);

    private final PrintService printService;
    private final SimpMessagingTemplate websocket;

    @Autowired
    public PrintingController(PrintService printService, SimpMessagingTemplate websocket)
    {
        this.printService = printService;
        this.websocket = websocket;
    }

    @ModelAttribute("requester")
    public String getRequester(@AuthenticationPrincipal StaffMember user)
    {
        return user.getEmail();
    }

    @GetMapping
    public String index()
    {
        return "admin/print/index";
    }

    @PostMapping("/request")
    @ResponseBody
    public ResponseEntity<ApiResponse> submitRequest(PrintSubmission submission)
    {
        try
        {
            PrintRequest req = printService.createRequest(submission);
            websocket.convertAndSend("/topic/print/queue", new PrintQueueUpdate(printService.getOpenRequests()));
            return ResponseEntity.ok(new ApiResponse(true, "Request submitted"));
        }
        catch (IOException e)
        {
            log.warn("Failed to submit print request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/dl/{id}")
    public void getRequestData(@PathVariable long id, HttpServletResponse response) throws IOException
    {
        printService.writeData(id, response);
    }

    @GetMapping("/request")
    @ResponseBody
    public List<PrintRequest> getRequests()
    {
        return printService.getOpenRequests();
    }

    @PostMapping("/request/{id}/claim")
    @ResponseBody
    public ResponseEntity<ApiResponse> claimRequest(@PathVariable long id)
    {
        printService.setRequestStatus(id, PrintRequest.Status.CLAIMED);
        websocket.convertAndSend("/topic/print/queue", new PrintQueueUpdate(printService.getOpenRequests()));
        return ResponseEntity.accepted().body(new ApiResponse(true, "Request claimed"));
    }

    @PostMapping("/request/{id}/complete")
    @ResponseBody
    public ResponseEntity<ApiResponse> completeRequest(@PathVariable long id)
    {
        printService.setRequestStatus(id, PrintRequest.Status.COMPLETED);
        websocket.convertAndSend("/topic/print/queue", new PrintQueueUpdate(printService.getOpenRequests()));
        return ResponseEntity.accepted().body(new ApiResponse(true, "Request completed"));
    }
}
