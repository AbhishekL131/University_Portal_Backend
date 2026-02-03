package com.example.College_Management_Portal.Controller;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import com.example.College_Management_Portal.Models.Student;
import com.example.College_Management_Portal.Models.StudentCourse;
import com.example.College_Management_Portal.Models.FacultyCourse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.College_Management_Portal.DTOs.MessageResponseDto;
import com.example.College_Management_Portal.Models.Attendance;
import com.example.College_Management_Portal.Models.AttendanceDisplay;
import com.example.College_Management_Portal.Models.Course;
import com.example.College_Management_Portal.Models.ExamScoreCardDto;
import com.example.College_Management_Portal.Models.Faculty;
import com.example.College_Management_Portal.Models.FacultyDto;
import com.example.College_Management_Portal.Models.Message;
import com.example.College_Management_Portal.Service.AttendanceService;
import com.example.College_Management_Portal.Service.CourseService;
import com.example.College_Management_Portal.Service.ExamScoreCardService;
import com.example.College_Management_Portal.Service.FacultyCourseService;
import com.example.College_Management_Portal.Service.FacultyService;
import com.example.College_Management_Portal.Service.MessageService;
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

    @Autowired
    private MessageService messageService;


    @GetMapping
    public ResponseEntity<?> getStudent(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Student student = studentService.getStudentByUserName(auth.getName());

        if(student != null){
            return new ResponseEntity<>(student,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/allcourses")
    public ResponseEntity<List<Course>> getAllCoursesOfStudent(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = studentService.getStudentByUserName(auth.getName()).getStudentId();

        List<StudentCourse> courses = studentCourseService.getAllCoursesOfStudent(studentId);

        List<String> courseIDs = courses
        .stream()
        .map(sc -> sc.getCourseId())
        .distinct()
        .collect(Collectors.toList());
        List<Course> StudentCourses = courseService.getAllStudentCoursesWithIDs(studentId,courseIDs);

        if(StudentCourses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(StudentCourses,HttpStatus.OK);
    }



    @GetMapping("/allfaculties")
    public ResponseEntity<?> getAllFacultiesOfStudent(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = studentService.getStudentByUserName(auth.getName()).getStudentId();
        List<StudentCourse> studentCourses = studentCourseService.getAllCoursesOfStudent(studentId);
        
        if (studentCourses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<String> courseIds = studentCourses.stream()
            .map(StudentCourse::getCourseId)
            .distinct()
            .collect(Collectors.toList());

        List<FacultyCourse> facultyCourses = facultyCourseService.getFacultiesForCourses(courseIds);

        List<String> facultyIds = facultyCourses.stream()
            .map(FacultyCourse::getFacultyId)
            .distinct()
            .collect(Collectors.toList());

        List<Faculty> faculties = facultyService.getFacultiesByIds(facultyIds);


        if(faculties.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(faculties.stream().map(FacultyDto::fromEntity).toList(), HttpStatus.OK);
    
    }

    @GetMapping("/getAttendance/{courseId}")
    public ResponseEntity<?> getAttendanceForCourse(@PathVariable String courseId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = studentService.getStudentByUserName(auth.getName()).getStudentId();
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
        String studentId = studentService.getStudentByUserName(auth.getName()).getStudentId();
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
        String studentId = studentService.getStudentByUserName(auth.getName()).getStudentId();
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
        String studentId = studentService.getStudentByUserName(auth.getName()).getStudentId();
        List<StudentCourse> studentCourses = studentCourseService.getAllCoursesOfStudent(studentId);
        List<ExamScoreCardDto> Results = examScoreCardService.getAllExamScoreCards(studentId,studentCourses);
        
        return new ResponseEntity<>(Results,HttpStatus.OK);
    }


    //// new feature to be rolled out here

    @GetMapping("/getPercentage")
    public ResponseEntity<?> getPercentageMarksOfStudent(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = studentService.getStudentByUserName(auth.getName()).getStudentId();
        List<StudentCourse> studentCourse = studentCourseService.getAllCoursesOfStudent(studentId);
        long totalMarks = studentCourse.stream()
        .map(sc -> examScoreCardService.getStudentExamScoreCard(studentId,sc.getCourseId()))
        .mapToInt(examSC -> examSC.getMaxMarks())
        .sum();

        long obtainedMarks = studentCourse
        .stream()
        .map(sc -> examScoreCardService.getStudentExamScoreCard(studentId,sc.getCourseId()))
        .mapToInt(examSc -> examSc.getObtainedMarks())
        .sum();

        float percent = ((float) obtainedMarks/totalMarks)*100;

        return ResponseEntity.ok(percent);
    }

    @GetMapping("/AllMessages")
    public ResponseEntity<List<MessageResponseDto>> getAllMessagesOfStudent(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = studentService.getStudentByUserName(auth.getName()).getStudentId();
        List<Message> messages = messageService.getAllMessagesOfReceiver(studentId);

        List<MessageResponseDto> response = messages
        .stream()
        .map(m -> 
            MessageResponseDto.builder()
            .title(m.getTitle())
            .content(m.getContent())
            .senderId(m.getSenderId())
            .createdAt(m.getCreatedAt())
            .build()
        )
        .toList();
        
        return ResponseEntity.ok(response);
    }
}
