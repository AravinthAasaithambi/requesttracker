package com.api.requesttracker.controllers;

import com.api.requesttracker.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/testMail")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text
    ) {
        try {
            emailService.sendEmail(to, subject, text);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "Email sent successfully!";
    }
}
