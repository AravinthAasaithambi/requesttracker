package com.api.requesttracker.controllers;

import com.api.requesttracker.entity.Requests;
import com.api.requesttracker.security.services.UserSession;
import com.api.requesttracker.serviceimpl.MailServiceImpl;
import com.api.requesttracker.services.CreateRequestService;
import com.api.requesttracker.services.EmailService;
import com.api.requesttracker.services.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/createRequest")
public class CreateRequestController {
    private static final Logger logger = LoggerFactory.getLogger(CreateRequestController.class);

    private final CreateRequestService requestService;

    @Autowired
    private MailServiceImpl mailService;


    public CreateRequestController(CreateRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping(value = "/createRequest" ,consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Requests createRequests(@RequestParam("assignToId") Long assignToId,
                                   @RequestParam("description") String description,
                                   @RequestParam("file") MultipartFile file,
                                   @RequestParam("imageFile") MultipartFile imageFile,
                                   @RequestParam("priority")Requests.Priority priority,
                                   @RequestParam("status") Requests.Status status,
                                   @RequestParam("title") String title,
                                   @RequestParam("videoLink") String videoLink){
        logger.info("API for Creating a new Request");
        Requests request =requestService.createRequests(assignToId,description,file,imageFile,priority,status,title,videoLink);
        if(request.getAssignedToID().equals(null)) {
            mailService.newRequestToAdmin(request);
        } else {
            mailService.requestAssignEmail(request);
            mailService.newRequestToAdmin(request);
        }
        logger.info("New Request Creation API Executed");
        return ResponseEntity.ok().body(request).getBody();
    }

}
