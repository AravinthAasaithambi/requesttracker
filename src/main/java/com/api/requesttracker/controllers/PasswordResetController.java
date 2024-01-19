package com.api.requesttracker.controllers;

import ch.qos.logback.core.model.Model;
import com.api.requesttracker.dto.PasswordResetRequestDTO;
import com.api.requesttracker.services.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passwordReset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam("email") String email) {
        passwordResetService.sendResetEmail(email);
        return ResponseEntity.ok("Password reset email sent successfully.");
    }
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully.");
    }
    @GetMapping("/form")
    public String showResetForm() {
        return "resetForm";
    }

    @PostMapping("/submit")
    public String submitResetForm(@RequestParam String token, @RequestParam String newPassword) {
        // Handle the form submission using the provided token and new password
        passwordResetService.resetPassword(token, newPassword);
        return "Password Reseted Successfully"; // Redirect to a success page or handle accordingly
    }
}
