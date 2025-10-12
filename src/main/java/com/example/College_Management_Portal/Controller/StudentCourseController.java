package com.example.College_Management_Portal.Controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.College_Management_Portal.Models.*;
import com.example.College_Management_Portal.Service.CourseService;
import com.example.College_Management_Portal.Service.StudentCourseService;
import com.example.College_Management_Portal.Service.StudentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/studentCourse")
@Slf4j
public class StudentCourseController {
    
    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;


    @PostMapping("/create")
    public ResponseEntity<StudentCourse> createNewEnrollment(@RequestBody StudentCourse studentCourse){
        String studentId = studentCourse.getStudentId();
        String courseId = studentCourse.getCourseId();
        Optional<Student> student = studentService.getStudentById(studentId);
        Optional<Course> course = courseService.getCourseById(courseId);
        if(!student.isPresent()){
            log.info("student with studentid "+studentId+" doesnt exist");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else if(!course.isPresent()){
            log.info("course with course id "+courseId+" doesnt exist");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else if(studentCourseService.checkIfEntryExists(studentId,courseId)){
            log.info("student is already enrolled in the course");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            studentCourseService.EnrollNewStudentInCourse(studentCourse);
            log.info("Enrollment created successfully ");
            return new ResponseEntity<>(studentCourse,HttpStatus.CREATED);
        }
    }



}
