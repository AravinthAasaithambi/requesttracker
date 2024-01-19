package com.api.requesttracker.serviceimpl;

import com.api.requesttracker.entity.User;
import com.api.requesttracker.repository.UserRepository;
import com.api.requesttracker.services.EmailService;
import com.api.requesttracker.services.PasswordResetService;
import com.api.requesttracker.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private EmailService emailService;

    // Simulated database for storing token and email associations
    private Map<String, String> tokenEmailMap = new HashMap<>();

    public PasswordResetServiceImpl(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public void sendResetEmail(String email) {
        String mail = email;
        String token = tokenService.generateToken();

        // Simulated database entry for token and email association
        tokenEmailMap.put(token, mail);

        // Send reset email
        String resetLink = "https://requesttracker.onrender.com/reset?token=" + token;
        //https://requesttracker.onrender.com/swagger-ui/index.html#/
        String emailText = "Click the following link to reset your password: " + resetLink;
        try {
            emailService.sendEmail(email, "Password Reset", emailText);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        if (tokenService.isValidToken(token)) {
            String email = tokenEmailMap.get(token);

            // Retrieve the user from the database using the email address
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // TODO: Hash and set the new password
                user.setPassword(encodePassword(newPassword)); // Assuming encodePassword is a method to hash the password

                // TODO: Save the updated user in the database
                userRepository.save(user);

                // Remove the token from the map after successful password reset
                tokenEmailMap.remove(token);
            } else {
                throw new RuntimeException("User not found for email: " + email);
            }
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }
    private String encodePassword(String rawPassword) {
        // Use BCryptPasswordEncoder to encode the password
        return encoder.encode(rawPassword);
    }

    @Override
    public ResponseEntity<?> changePassword(Long userId, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return null;
    }
    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}

