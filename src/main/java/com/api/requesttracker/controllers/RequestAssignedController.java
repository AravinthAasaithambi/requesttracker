package com.api.requesttracker.controllers;

import com.api.requesttracker.entity.Requests;
import com.api.requesttracker.services.RequestAssignedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/requestAssigned")
public class RequestAssignedController {
    private static final Logger logger = LoggerFactory.getLogger(RequestAssignedController.class);

    private final RequestAssignedService requestService;

    public RequestAssignedController(RequestAssignedService requestService) {
        this.requestService = requestService;
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

}
