package com.example.College_Management_Portal.Config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;




@Aspect
@Component
@Slf4j
public class RateLimitAspect {

    @Autowired
    private RedisRateLimiter redisRateLimiter;

    @Autowired
    private HttpServletRequest request;

    @Around("@annotation(rateLimit)")
public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
    String clientIp = request.getRemoteAddr();
    String key = clientIp + ":" + joinPoint.getSignature().getName();

    boolean allowed = redisRateLimiter.isAllowed(key, rateLimit.limit(), rateLimit.duration());
    if (!allowed) {
        log.warn("Too may requests from IP : {} for method : {}",clientIp,joinPoint.getSignature().getName());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .body("too many requests . please try after some time ");
    }

    return joinPoint.proceed();
}


}
