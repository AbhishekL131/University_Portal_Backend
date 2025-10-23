package com.example.College_Management_Portal.Controller;



import java.util.List;
import java.util.Optional;
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
import com.example.College_Management_Portal.Models.Student;
import com.example.College_Management_Portal.Service.CourseService;

import com.example.College_Management_Portal.Service.FacultyCourseService;
import com.example.College_Management_Portal.Service.FacultyService;
import com.example.College_Management_Portal.Service.StudentCourseService;
import com.example.College_Management_Portal.Service.StudentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/Faculty")
@Slf4j
@Tag(name = "Faculty APIs")
public class FacultyController {
    
    @Autowired
    private FacultyService facultyService;

    
    @Autowired
    private FacultyCourseService facultyCourseService;

    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;


    @GetMapping
    public ResponseEntity<Faculty> getFaculty(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String facultyId = facultyService.getFacultyByUserName(auth.getName()).map(x -> x.getFacultyId()).orElse(null);
        Optional<Faculty> faculty = facultyService.getFacultyById(facultyId);
        if(faculty != null){
            return new ResponseEntity<>(faculty.get(),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/allcourses")
    public ResponseEntity<List<Course>> getAllCoursesOfFaculty(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String facultyId = facultyService.getFacultyByUserName(auth.getName()).map(faculty -> faculty.getFacultyId()).orElse(null);
        return facultyService.getFacultyById(facultyId)
        .map(faculty -> {
            List<Course> courses = facultyCourseService.getAllCoursesOfFaculty(facultyId)
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


    @GetMapping("/allStudents")
    public ResponseEntity<List<Student>> getAllStudentsOfFaculty(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String facultyId = facultyService.getFacultyByUserName(auth.getName()).map(x -> x.getFacultyId()).orElse(null);
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
}
