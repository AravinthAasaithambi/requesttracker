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

    @GetMapping("/getRequestAssingnedRequestByUser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Requests> getRequestAssingnedRequestByUser() {
        logger.info("API to Get Request Assigned By User");
        List<Requests> result=requestService.assingnedRequestByUser();
        logger.info("API Executed");
        return ResponseEntity.ok().body(result).getBody();
    }
    @GetMapping("/getRequestAssingnedRequestToUser")
    @PreAuthorize("hasRole('USER')")
    public List<Requests> getRequestAssingnedRequestForUser() {
        logger.info("API to Get Request Assigned to User");
        List<Requests> result=requestService.assingnedRequestForUser();
        logger.info("API Executed");
        return ResponseEntity.ok().body(result).getBody();
    }
    @PutMapping("/upateStatus")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Requests upateStatus( @RequestParam("requestid") Long requestid ,@RequestParam("status") Requests.Status status){
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
    @GetMapping("/getAnalysisReport")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public RequestAnalysisCountDTO getAnalysisReport() {
        logger.info("API to Get Analysis of Request");
        RequestAnalysisCountDTO result=requestService.getAnnalysisReport();
        logger.info("API Executed");
        return ResponseEntity.ok().body(result).getBody();
    }
    @GetMapping("/getAnalysisReportDateRange")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public RequestAnalysisCountDTO getAnalysisReportDateRange(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate edDate) {
        logger.info("API to Get Analysis Report Based on Date Range of a Request");
        RequestAnalysisCountDTO result=requestService.getAnnalysisReportDateRange(startDate,edDate);
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
