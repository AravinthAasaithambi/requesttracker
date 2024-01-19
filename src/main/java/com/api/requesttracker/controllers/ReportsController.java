package com.api.requesttracker.controllers;

import com.api.requesttracker.dto.RequestAnalysisCountDTO;
import com.api.requesttracker.security.services.UserSession;
import com.api.requesttracker.serviceimpl.MailServiceImpl;
import com.api.requesttracker.services.EmailService;
import com.api.requesttracker.services.ReportsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);

    private final ReportsService requestService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private UserSession userSession;
    public ReportsController(ReportsService requestService) {
        this.requestService = requestService;
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
}
