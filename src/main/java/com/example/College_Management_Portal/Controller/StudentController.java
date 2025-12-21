package com.example.College_Management_Portal.Controller;


import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import com.example.College_Management_Portal.Models.Student;
import com.example.College_Management_Portal.Models.StudentCourse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.College_Management_Portal.Models.Attendance;
import com.example.College_Management_Portal.Models.AttendanceDisplay;
import com.example.College_Management_Portal.Models.Course;
import com.example.College_Management_Portal.Models.ExamScoreCardDto;
import com.example.College_Management_Portal.Models.Faculty;
import com.example.College_Management_Portal.Models.FacultyDto;
import com.example.College_Management_Portal.Service.AttendanceService;
import com.example.College_Management_Portal.Service.CourseService;
import com.example.College_Management_Portal.Service.ExamScoreCardService;
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

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private ExamScoreCardService examScoreCardService;


    @GetMapping
    public ResponseEntity<?> getStudent(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Student> student = studentService.getStudentByUserName(auth.getName());

        if(student.isPresent()){
            return new ResponseEntity<>(student.get(),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
    public ResponseEntity<?> getAllFacultiesOfStudent(){
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

            return new ResponseEntity<>(faculties.stream().map(faculty -> FacultyDto.fromEntity(faculty)).toList(),HttpStatus.OK);
        })
        .orElseGet(() -> {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }

    @GetMapping("/getAttendance/{courseId}")
    public ResponseEntity<?> getAttendanceForCourse(@PathVariable String courseId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = studentService.getStudentByUserName(auth.getName()).map(student -> student.getStudentId()).orElse(null);
        List<Attendance> attd = studentCourseService.getStudentAttendance(studentId,courseId);
        if(attd != null){
            return new ResponseEntity<>(attd.stream().map(x -> AttendanceDisplay.fromEntity(x)).toList(),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAttendancePercent/{courseId}")
    public ResponseEntity<?> getAttendancePercentForCourse(@PathVariable String courseId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = studentService.getStudentByUserName(auth.getName()).map(student -> student.getStudentId()).orElse(null);
        Optional<StudentCourse> studentCourse = studentCourseService.getStudentCourse(studentId,courseId);

        if(studentCourse.isPresent()){
            List<Attendance> attendance = attendanceService.getByStudentCourseId(studentCourse.get().getId());
            long totalClasses = attendance.stream().count();
            long attendedClasses = attendance.stream().filter(x -> x.getPresent().equals(true)).count();

            float attendancePercent = ((float)attendedClasses/totalClasses)*100;

            return new ResponseEntity<>(attendancePercent,HttpStatus.OK);
        }

        return new ResponseEntity<>(0,HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/getScoreCard/{courseId}")
    public ResponseEntity<?> getExamScoreCardForCourse(@PathVariable String courseId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = studentService.getStudentByUserName(auth.getName()).map(student -> student.getStudentId()).orElse(null);
        ExamScoreCardDto examScoreCard = examScoreCardService.getStudentExamScoreCard(studentId,courseId);
        Optional<StudentCourse> studentCourse = studentCourseService.getStudentCourse(studentId,courseId);
        if(studentCourse.isPresent()){
            return new ResponseEntity<>(examScoreCard,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllScoreCards")
    public ResponseEntity<?> getAllExamScoreCardsOfStudent(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = studentService.getStudentByUserName(auth.getName()).map(student -> student.getStudentId()).orElse(null);
        List<StudentCourse> studentCourse = studentCourseService.getAllCoursesOfStudent(studentId);
        List<ExamScoreCardDto> examScoreCards = studentCourse.stream()
        .map(sc -> examScoreCardService.getStudentExamScoreCard(studentId,sc.getCourseId()))
        .toList();

        if(examScoreCards.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(examScoreCards,HttpStatus.OK);
        }
    }
}
