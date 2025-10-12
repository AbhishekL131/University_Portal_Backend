package com.example.College_Management_Portal.Config;



import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisRateLimiter {

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    public boolean isAllowed(String key, int limit, int duration) {
        Long count = redisTemplate.opsForValue().increment(key);
        System.out.println("No of requests : "+count);
        if (count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(duration));
        }
        return count <= limit;
    }
}
