package com.api.requesttracker.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PasswordResetService {
    void sendResetEmail(String email);

    void resetPassword(String token, String newPassword);

    ResponseEntity<?> changePassword(Long userId, String newPassword);
}
