package com.api.requesttracker.services;

import com.api.requesttracker.dto.PasswordResetRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface PasswordResetService {
    void sendResetEmail(String email);

    void resetPassword(String token, String newPassword);
}
