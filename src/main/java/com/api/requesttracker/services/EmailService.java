package com.api.requesttracker.services;

import jakarta.mail.internet.InternetAddress;
import jakarta.persistence.criteria.From;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

@Service
public class EmailService {

    //private final Logger logger = (Logger) LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) throws UnsupportedEncodingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(String.valueOf(new InternetAddress("aravinth.a@telliant.net","Request Tracker")));
        javaMailSender.send(message);
    //logger.info("Email Sent Successfully for the User "+to);
    //logger.info(""+message);
    }
}
