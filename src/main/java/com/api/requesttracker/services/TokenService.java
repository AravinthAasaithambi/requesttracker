package com.api.requesttracker.services;

import org.springframework.stereotype.Service;

@Service
public interface TokenService {
        String generateToken();
        boolean isValidToken(String token);
    }

