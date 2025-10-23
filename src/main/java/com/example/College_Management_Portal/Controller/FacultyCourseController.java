package com.example.College_Management_Portal.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.College_Management_Portal.Models.FacultyCourse;
import com.example.College_Management_Portal.Service.CourseService;

import com.example.College_Management_Portal.Service.FacultyCourseService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/facultyCourse")
@Slf4j
@Tag(name = "Faculty-Course APIs")
public class FacultyCourseController {
    
    @Autowired
    private FacultyCourseService facultyCourseService;


    @Autowired
    private CourseService courseService;


    @PostMapping("/createFacultyCourse")
    public ResponseEntity<?> createNewFacultyCourse(@RequestBody FacultyCourse facultyCourse){
        return courseService.getCourseById(facultyCourse.getCourseId())
        .map(fc -> {
            facultyCourseService.createFacultyCourseEntry(facultyCourse);
            return new ResponseEntity<>(HttpStatus.OK);
        })
        .orElseGet(() -> {
            log.info("Course "+facultyCourse.getCourseId()+" doesnt exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
        
    }
}
