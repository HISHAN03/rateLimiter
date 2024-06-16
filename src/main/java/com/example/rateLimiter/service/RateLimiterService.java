package com.example.rateLimiter.service;
import com.example.rateLimiter.util.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private final RateLimiter rateLimiter;

    public RateLimiterService(
            @Value("${spring.redis.host}") String redisHost,
            @Value("${spring.redis.port}") int redisPort,
            @Value("${rateLimiter.bucketSize:10}") int bucketSize,
            @Value("${rateLimiter.refillRate:1.0}") double refillRate) {

        this.rateLimiter = new RateLimiter(redisHost, redisPort, bucketSize, refillRate);
    }

    public boolean isRequestAllowed(String clientId) {
        return rateLimiter.allowRequest(clientId);
    }
}
