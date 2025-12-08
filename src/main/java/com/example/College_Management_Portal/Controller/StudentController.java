package com.example.College_Management_Portal.Controller;


import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.College_Management_Portal.Models.Course;
import com.example.College_Management_Portal.Models.Faculty;

import com.example.College_Management_Portal.Service.CourseService;
import com.example.College_Management_Portal.Service.FacultyCourseService;
import com.example.College_Management_Portal.Service.FacultyService;
import com.example.College_Management_Portal.Service.StudentCourseService;
import com.example.College_Management_Portal.Service.StudentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/Student")
@Slf4j
@Tag(name = "Student APIs")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FacultyCourseService facultyCourseService;
    

    @GetMapping("/allcourses")
    public ResponseEntity<List<Course>> getAllCoursesOfStudent(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = studentService.getStudentByUserName(auth.getName()).map(x -> x.getStudentId()).orElseGet(null);
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

    @GetMapping("/allfaculties")
    public ResponseEntity<List<Faculty>> getAllFacultiesOfStudent(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = studentService.getStudentByUserName(auth.getName()).map(x -> x.getStudentId()).orElse(null);
        return studentService.getStudentById(studentId)
        .map(student -> {
            List<Faculty> faculties = studentCourseService.getAllCoursesOfStudent(studentId)
            .stream()
            .map(studentCourse -> courseService.getCourseById(studentCourse.getCourseId()))
            .filter(course -> course.isPresent())
            .map(course -> course.get())
            .flatMap(course -> facultyCourseService.getAllFacultiesOfCourse(course.getCourseId()).stream())
            .map(facultyCourse -> facultyService.getFacultyById(facultyCourse.getFacultyId()))
            .filter(faculty -> faculty.isPresent())
            .map(faculty -> faculty.get())
            .collect(Collectors.toList());

            return new ResponseEntity<>(faculties,HttpStatus.OK);
        })
        .orElseGet(() -> {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }
}
