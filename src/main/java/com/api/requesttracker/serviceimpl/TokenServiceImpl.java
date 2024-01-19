package com.api.requesttracker.serviceimpl;

import com.api.requesttracker.services.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean isValidToken(String token) {
        // TODO: Implement logic to check if the token is valid (e.g., check against the database)
        // For simplicity, always return true in this example
        return true;
    }
}