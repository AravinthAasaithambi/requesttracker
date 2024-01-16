package com.api.requesttracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.api.requesttracker")
public class RequesttrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RequesttrackerApplication.class, args);
    }

}
