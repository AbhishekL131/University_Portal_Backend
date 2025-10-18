package com.example.College_Management_Portal.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.College_Management_Portal.Config.RateLimit;
import com.example.College_Management_Portal.Models.*;
import com.example.College_Management_Portal.Service.CourseService;
import com.example.College_Management_Portal.Service.DepartmentService;
import com.example.College_Management_Portal.Service.FacultyService;
import com.example.College_Management_Portal.Service.StudentCourseService;
import com.example.College_Management_Portal.Service.StudentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/department")
@Slf4j
public class DepartmentController {
    
    @Autowired
    private DepartmentService deptService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentCourseService studentCourseService;


    @PostMapping("/create")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department){
       return deptService.getDepartmentById(department.getDeptId())
       .map(dept-> {
        deptService.createNewDepartment(department);
        return new ResponseEntity<>(department,HttpStatus.OK);
       })
       .orElseGet(() -> {
        log.info("department "+department.getDeptId()+" doesnt exist ");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       });
    }


    @GetMapping("/getHod/{deptId}")
    public ResponseEntity<?> getHodOfDepartment(@PathVariable String deptId){
        return deptService.getDepartmentById(deptId)
        .map(dept -> {
            Optional<Faculty> faculty = facultyService.getAllByDeptId(deptId)
            .stream()
            .filter(f -> f.getRoles().contains("HOD"))
            .findFirst();
            return new ResponseEntity<>(faculty.get(),HttpStatus.FOUND);
        })
        .orElseGet(() -> {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }


    @GetMapping("/allfaculties/{deptId}")
    public ResponseEntity<List<Faculty>> getAllFaculties(@PathVariable String deptId){
       return deptService.getDepartmentById(deptId)
       .map(dept -> {
        List<Faculty> faculties = facultyService.getAllByDeptId(deptId)
        .stream()
        .collect(Collectors.toList());
        return new ResponseEntity<>(faculties,HttpStatus.OK);
       })
       .orElseGet(() -> {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       });


    }


    @GetMapping("/getAllHod")
    public ResponseEntity<List<Faculty>> getAllHOD(){
        List<Faculty> faculties = facultyService.getAllFaculties()
        .stream()
        .filter(f -> f.getRoles().contains("HOD"))
        .collect(Collectors.toList());
        if(!faculties.isEmpty()){
            return new ResponseEntity<>(faculties,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @RateLimit(limit=5,duration=60)
      @GetMapping("/allcourses/{deptId}")
      public ResponseEntity<List<Course>> getAllCoursesOfDept(@PathVariable String deptId){
        return deptService.getDepartmentById(deptId)
        .map(dept -> {
            List<Course> courses = courseService.getByDeptId(deptId)
            .stream()
            .collect(Collectors.toList());
            return ResponseEntity.ok(courses);
        })
        .orElseGet(() -> {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        });
    }


     @GetMapping("/allstudents/{deptId}")
    public ResponseEntity<List<Student>> getAllStudentsOfDept(@PathVariable String deptId){
        return deptService.getDepartmentById(deptId)
        .map(dept -> {
            List<Student> students = studentService.getAllStudentsOfDepartment(deptId)
            .stream()
            .collect(Collectors.toList());
            return new ResponseEntity<>(students,HttpStatus.OK);
        })
        .orElseGet(() -> {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        });
    }


    @DeleteMapping("/deletestudent/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable String studentId){
        return studentService.getStudentById(studentId)
        .map(student -> {
            studentService.deleteStudentById(studentId);
            studentCourseService.deleteStudentCourseEntry(studentId);
            log.info("deleted student successfully");
            return new ResponseEntity<>(HttpStatus.OK);
        })
        .orElseGet(() -> {
            log.info("student not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        });
    }

    @DeleteMapping("/deleteDepartment/{deptId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable String deptId){
        return deptService.getDepartmentById(deptId)
        .map(dept -> {
            deptService.deleteByDepartmentId(deptId);
            facultyService.deleteFacultyByDepartmentId(deptId);
            studentService.deleteStudentByDepartmentId(deptId);
            courseService.deleteCourseByDepartmentId(deptId);

            return new ResponseEntity<>(HttpStatus.OK);
        })
        .orElseGet(() -> {
            log.info("department "+deptId+" doesn't exist");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        });
    }
}
