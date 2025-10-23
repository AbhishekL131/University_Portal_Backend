package com.example.College_Management_Portal.Controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.College_Management_Portal.Config.RateLimit;
import com.example.College_Management_Portal.Models.Course;

import com.example.College_Management_Portal.Models.Faculty;

import com.example.College_Management_Portal.Models.Student;

import com.example.College_Management_Portal.Service.CourseService;
import com.example.College_Management_Portal.Service.FacultyCourseService;
import com.example.College_Management_Portal.Service.FacultyService;
import com.example.College_Management_Portal.Service.StudentCourseService;
import com.example.College_Management_Portal.Service.StudentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "Course APIs")
@RequestMapping("/Course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultyCourseService facultyCourseService;

    @Autowired
    private FacultyService facultyService;

    
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable String courseId){
       return courseService.getCourseById(courseId)
       .map(course -> {
        log.info("reading from db for course : {}"+courseId);
        return new ResponseEntity<>(course,HttpStatus.OK);
       })
       .orElseGet(() -> {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       });
    }


    @GetMapping("/courseCredits/{courseCredits}")
    public ResponseEntity<List<Course>> getAllCoursesByCredits(@PathVariable int courseCredits){
        List<Course> courses = courseService.getByCredits(courseCredits);
        if(!courses.isEmpty()){
            return new ResponseEntity<>(courses,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/allstudents/{courseId}")
    public ResponseEntity<List<Student>> getAllStudents(@PathVariable String courseId){
       return courseService.getCourseById(courseId)
       .map(course -> {
        List<Student> students = studentCourseService.getAllStudentsEnrolledInCourse(courseId)
        .stream()
        .map(x -> studentService.getStudentById(x.getStudentId()))
        .filter(x -> x.isPresent())
        .map(x -> x.get())
        .collect(Collectors.toList());

        return new ResponseEntity<>(students,HttpStatus.OK);
       })
       .orElseGet(() -> {
        log.info("course "+courseId+" is invalid or doesn't exist");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       });
    }


    
    @RateLimit(limit=5,duration=60)
    @GetMapping("/faculties/{courseId}")
    public ResponseEntity<List<Faculty>> getAllFacultiesForCourse(@PathVariable String courseId){
       return courseService.getCourseById(courseId)
       .map(course -> {
        List<Faculty> faculties = facultyCourseService.getAllFacultiesOfCourse(courseId)
        .stream()
        .map(fc -> facultyService.getFacultyById(fc.getFacultyId()))
        .filter(x -> x.isPresent())
        .map(x -> x.get())
        .collect(Collectors.toList());

        return new ResponseEntity<>(faculties,HttpStatus.OK);
       })
       .orElseGet(() -> {
        log.info("course doesnt exist",courseId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       });
    }


}

