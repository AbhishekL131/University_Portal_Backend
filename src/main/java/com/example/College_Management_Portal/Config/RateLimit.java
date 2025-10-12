package com.example.College_Management_Portal.Config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
    int limit();
    int duration(); // duration in seconds
}