package com.example.rateLimiter.controller;

import com.example.rateLimiter.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class rateLimiterController {

    @Autowired
    private RateLimiterService rateLimiterService;

    @GetMapping("/hello")
    public String getHello(@RequestParam String clientId) {
        if (rateLimiterService.isRequestAllowed(clientId)) {
            return "Hello";
        } else {
            return "Too many requests, please try again later.";
        }
    }
}
