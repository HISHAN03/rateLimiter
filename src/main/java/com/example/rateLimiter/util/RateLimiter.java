package com.example.rateLimiter.util;
import redis.clients.jedis.Jedis;
import java.time.Instant;

public class RateLimiter {

    private final Jedis jedis;
    private final int bucketSize;
    private final double refillRate;

    public RateLimiter(String redisHost, int redisPort, int bucketSize, double refillRate) {
        this.jedis = new Jedis(redisHost, redisPort);
        this.bucketSize = bucketSize;
        this.refillRate = refillRate;
    }

    private String getKey(String clientId) {
        return "rate_limiter:" + clientId;
    }

    private void refillTokens(String key) {
        long currentTime = Instant.now().getEpochSecond();
        String[] values = jedis.hmget(key, "lastRefill", "tokens").toArray(new String[0]);

        long lastRefill = values[0] == null ? currentTime : Long.parseLong(values[0]);
        double tokens = values[1] == null ? bucketSize : Double.parseDouble(values[1]);

        double newTokens = Math.min(bucketSize, tokens + (currentTime - lastRefill) * refillRate);

        jedis.hset(key, "tokens", String.valueOf(newTokens));
        jedis.hset(key, "lastRefill", String.valueOf(currentTime));
    }

    public boolean allowRequest(String clientId) {
        String key = getKey(clientId);
        refillTokens(key);
        double tokens = Double.parseDouble(jedis.hget(key, "tokens"));

        if (tokens >= 1) {
            jedis.hincrByFloat(key, "tokens", -1);
            return true;
        } else {
            return false;
        }
    }
}
