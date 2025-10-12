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

import com.example.College_Management_Portal.Models.Student;

import com.example.College_Management_Portal.Service.CourseService;
import com.example.College_Management_Portal.Service.StudentCourseService;
import com.example.College_Management_Portal.Service.StudentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/Student")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;


    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private CourseService courseService;


    @RateLimit(limit=5,duration=60)
    @GetMapping("/all")
    public List<Student> getAllStudents(){
        return studentService.allStudents();
    }
    

    @GetMapping("/allcourses/{studentId}")
    public ResponseEntity<List<Course>> getAllCoursesOfStudent(@PathVariable String studentId){
       return studentService.getStudentById(studentId)
       .map(student -> {
        List<Course> courses = studentCourseService.getAllCoursesOfStudent(studentId)
        .stream()
        .map(x -> courseService.getCourseById(x.getCourseId()))
        .filter(x -> x.isPresent())
        .map(x -> x.get())
        .collect(Collectors.toList());
        return new ResponseEntity<>(courses,HttpStatus.OK);
       })
       .orElseGet(() -> {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       });
    }
}
