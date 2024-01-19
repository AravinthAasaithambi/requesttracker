package com.api.requesttracker.controllers;

import com.api.requesttracker.dto.RequestAnalysisCountDTO;
import com.api.requesttracker.entity.Requests;
import com.api.requesttracker.repository.RequestRepository;
import com.api.requesttracker.security.services.UserDetailsImpl;
import com.api.requesttracker.security.services.UserSession;
import com.api.requesttracker.serviceimpl.MailServiceImpl;
import com.api.requesttracker.services.EmailService;
import com.api.requesttracker.services.RequestService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/request")
public class RequestsController {
    private static final Logger logger = LoggerFactory.getLogger(RequestsController.class);

    private final RequestService requestService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private UserSession userSession;
    public RequestsController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/getRequestById")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<Requests> getRequestById(@RequestParam Long id) {
        logger.info("API for Getting a Request Based on Request Id");
        Optional<Requests> result=requestService.findById(id);
        logger.info("API Executed");
        return ResponseEntity.ok().body(result).getBody();
    }

    @GetMapping("/getAllRequest")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Requests> getAllRequest() {
        logger.info("API for Getting all the Request");
        List<Requests> result=requestService.getAllRequest();
        logger.info("API Executed");
        return ResponseEntity.ok().body(result).getBody();
    }


    @PutMapping("/updateRequest")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Requests updateRequest( @Valid @RequestBody Requests request){
        logger.info("API for Updating a new Request");
        Requests result=requestService.updateRequest(request);
        logger.info("Update API Executed");
        return ResponseEntity.ok().body(result).getBody();
    }

    @DeleteMapping("/deleteRequest")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteRequest( @RequestParam("RequestId") Long requestId){
        logger.info("API for Deleting a Request");
        logger.info("Delete API Executed");
        return ResponseEntity.ok().body(requestService.deleteRequest(requestId)).getBody();
    }

}
