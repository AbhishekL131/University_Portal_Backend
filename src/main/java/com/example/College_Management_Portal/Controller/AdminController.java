package com.example.College_Management_Portal.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.College_Management_Portal.Models.Admin;
import com.example.College_Management_Portal.Models.Course;
import com.example.College_Management_Portal.Models.Faculty;
import com.example.College_Management_Portal.Models.Student;
import com.example.College_Management_Portal.Service.AdminService;
import com.example.College_Management_Portal.Service.CourseService;
import com.example.College_Management_Portal.Service.DepartmentService;
import com.example.College_Management_Portal.Service.FacultyService;
import com.example.College_Management_Portal.Service.StudentService;
import java.util.Map;
import java.util.HashMap;
import com.example.College_Management_Portal.Repository.CourseRepository;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/Admin")
@Slf4j
@Tag(name = "Admin APIs",description = "Admin Login controller under role based access control (RBAC)")
public class AdminController {

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private DepartmentService deptService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AdminService adminService;



    @PostMapping("/createFaculty")
    @Operation(summary = "create a new faculty")
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty){
       return deptService.getDepartmentById(faculty.getDeptId())
       .map(dept -> {
        facultyService.createNewFaculty(faculty);
            return new ResponseEntity<>(faculty,HttpStatus.CREATED);
       })
       .orElseGet(() -> {
        log.info("Department "+faculty.getDeptId()+" Does'nt exist ");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       });
    }


    @PostMapping("/createAdmin")
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin){
        adminService.createNewAdmin(admin);
        return new ResponseEntity<>(admin,HttpStatus.CREATED);
    }


    @PostMapping("/createHOD")
    public ResponseEntity<?> createHOD(@RequestBody Faculty faculty){
        return deptService.getDepartmentById(faculty.getDeptId())
        .map(dept -> {
           if(facultyService.departmentHasHOD(faculty.getDeptId())){
            return new ResponseEntity<>("Hod already exists",HttpStatus.CONFLICT);
           }
           facultyService.createDepartmentHod(faculty);
           return new ResponseEntity<>(faculty,HttpStatus.OK);
        })
        .orElseGet(() -> {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        });
    }

    @PostMapping("/createCourse")
    public ResponseEntity<Course> CreateCourse(@RequestBody Course course){
      return deptService.getDepartmentById(course.getDeptId())
      .map(dept -> {
        courseService.createNewCourse(course);
        return new ResponseEntity<>(course,HttpStatus.OK);
      })
      .orElseGet(() -> {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      });
    }


    @PostMapping("/createStudent")
    public ResponseEntity<Student> saveNewStudent(@RequestBody Student student){
        return deptService.getDepartmentById(student.getDepartment())
        .map(x -> {
            studentService.saveStudent(student);
            return new ResponseEntity<>(student,HttpStatus.OK);
        })
        .orElseGet(() -> {
            log.info("department "+student.getDepartment()+" Doesn't exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }



    @Autowired
    private CourseRepository courseRepo;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats(){
        Map<String, Long> stats = new HashMap<>();
        stats.put("faculty", facultyService.getAllFaculties().stream().count());
        stats.put("students", studentService.allStudents().stream().count());
        stats.put("courses", courseRepo.findAll().stream().count());
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
