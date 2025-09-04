package com.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class EduLogicVulApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduLogicVulApplication.class, args);
    }
}