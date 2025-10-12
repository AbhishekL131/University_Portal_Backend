package com.example.College_Management_Portal.CacheTests;

import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.College_Management_Portal.Models.Course;
import com.example.College_Management_Portal.Service.CourseService;

@RestController
@RequestMapping("/api/test-cache")
public class CacheTestController {
    
    @Autowired
    private CourseService courseService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Course> getCourse(@PathVariable String courseId){
        long start = System.currentTimeMillis();
        Optional<Course> course  = courseService.getCourseById(courseId);
        long finish = System.currentTimeMillis()-start;
        return ResponseEntity.ok()
        .header("X-Response-time-ms",String.valueOf(finish))
        .body(course.get());
    }
}
