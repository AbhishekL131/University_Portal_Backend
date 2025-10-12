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
import com.example.College_Management_Portal.Models.*;

import com.example.College_Management_Portal.Service.*;

@RestController
@RequestMapping("/facultyStudent")
public class FacultyStudentController {
    @Autowired
    private FacultyCourseService facultyCourseService;

    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;


    @RateLimit(limit=5,duration=60)
    @GetMapping("/allStudents/{facultyId}")
    public ResponseEntity<List<Student>> getAllStudentsOfFaculty(@PathVariable String facultyId){
        return facultyService.getFacultyById(facultyId)
        .map(faculty -> {
            List<Student> students = facultyCourseService.getAllCoursesOfFaculty(facultyId)
            .stream()
            .map(fc -> courseService.getCourseById(fc.getCourseId()))
            .filter(x -> x.isPresent())
            .map(x -> x.get())
            .flatMap(course -> studentCourseService.getAllStudentsEnrolledInCourse(course.getCourseId()).stream())
            .map(sc -> studentService.getStudentById(sc.getStudentId()))
            .filter(x -> x.isPresent())
            .map(x -> x.get())
            .collect(Collectors.toList());

            return new ResponseEntity<>(students,HttpStatus.OK);
        })
        .orElseGet(() -> {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        });
    }





    @GetMapping("/allfaculties/{studentId}")
    public ResponseEntity<List<Faculty>> getAllFacultiesOfStudent(@PathVariable String studentId){
        return studentService.getStudentById(studentId)
        .map(student -> {
            List<Faculty> faculties = studentCourseService.getAllCoursesOfStudent(studentId)
            .stream()
            .map(sc -> courseService.getCourseById(sc.getCourseId()))
            .filter(c -> c.isPresent())
            .map(c -> c.get())
            .flatMap(fc -> facultyCourseService.getAllFacultiesOfCourse(fc.getCourseId()).stream())
            .map(x -> facultyService.getFacultyById(x.getFacultyId()))
            .filter(f -> f.isPresent())
            .map(f -> f.get())
            .collect(Collectors.toList());
            return new ResponseEntity<>(faculties,HttpStatus.OK);
        })
        .orElseGet(() -> {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        });
    }
}
