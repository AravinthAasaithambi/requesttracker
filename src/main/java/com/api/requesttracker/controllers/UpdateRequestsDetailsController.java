package com.api.requesttracker.controllers;

import com.api.requesttracker.entity.Requests;
import com.api.requesttracker.security.services.UserDetailsImpl;
import com.api.requesttracker.security.services.UserSession;
import com.api.requesttracker.serviceimpl.MailServiceImpl;
import com.api.requesttracker.services.EmailService;
import com.api.requesttracker.services.RequestService;
import com.api.requesttracker.services.UpdateRequestsDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/updateRequestDetails")
public class UpdateRequestsDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateRequestsDetailsController.class);

    private final UpdateRequestsDetailsService requestService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private UserSession userSession;
    public UpdateRequestsDetailsController(UpdateRequestsDetailsService requestService) {
        this.requestService = requestService;
    }


    @PutMapping("/upateStatus")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Requests upateStatus(@RequestParam("requestid") Long requestid , @RequestParam("status") Requests.Status status){
        logger.info("API to Update Status of a Request");
        Requests result=requestService.changeStatus(requestid ,status);
        logger.info("API Executed");
        return ResponseEntity.ok().body(result).getBody();
    }
    @PutMapping("/updatePriority")
    @PreAuthorize("hasRole('ADMIN')")
    public Requests updatePriority( @RequestParam("requestid") Long requestid ,@RequestParam("status") Requests.Priority priority){
        logger.info("API to Update Priority of a Request");
        Requests result=requestService.changePriority(requestid,priority);
        logger.info("API Executed");
        return ResponseEntity.ok().body(result).getBody();
    }

    @PutMapping("/assignRequest")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignRequest( @RequestParam("userId") Long userId, @RequestParam("requestId") Long requestId ){
        logger.info("API to Assign a new Request ");
        String result=requestService.assignRequest(userId,requestId);
        if (result.equalsIgnoreCase("Request assigned successfully.")){
            Requests request = requestService.getRequestById(requestId);
            logger.info("Mailer Functionality inform User about Request");
            mailService.requestAssignEmail(request);
        }
        logger.info("API Executed");
        return ResponseEntity.ok().body(result).getBody();
    }

    @PutMapping("/cancelRequest")
    @PreAuthorize("hasRole('USER')")
    public Requests cancelRequest( @RequestParam("requestId") Long requestId ){
        logger.info("Cancel request API called for cancelling the Request Id" + requestId);
        Requests result=requestService.rejectRequest(requestId);
        UserDetailsImpl userdetail= userSession.getUserDetails();
        Long rejectedByUserId = userdetail.getId();
        logger.info("Request Has been Cancelled");
        logger.info("Mailer Called for informing Cancellation to Admin");
        mailService.cancelRequest(rejectedByUserId,result);
        return ResponseEntity.ok().body(result).getBody();
    }
}
