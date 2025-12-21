package com.example.College_Management_Portal.Service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.*;
import com.example.College_Management_Portal.Repository.AttendanceRepository;
import com.example.College_Management_Portal.Repository.StudentCourseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentCourseService {
    
    @Autowired
    private StudentCourseRepository studentCourseRepo;

    @Autowired
    private AttendanceRepository attendanceRepo;


    public void EnrollNewStudentInCourse(StudentCourse studentCourse){
        studentCourseRepo.save(studentCourse);
    }
    public List<StudentCourse> getAllStudentsEnrolledInCourse(String courseId){
        return studentCourseRepo.findStudentByCourseId(courseId);
    }

    public List<StudentCourse> getAllCoursesOfStudent(String studentId){
        return studentCourseRepo.findCourseByStudentId(studentId);
    }

    public boolean checkIfEntryExists(String studentId,String courseId){
        return studentCourseRepo.existsByStudentIdAndCourseId(studentId,courseId);
    }

    public void deleteStudentCourseEntry(String studentId){
        studentCourseRepo.deleteByStudentId(studentId);
    }


   public List<Attendance> getStudentAttendance(String studentId,String courseId){
        Optional<StudentCourse> studentCourse = studentCourseRepo.findByStudentIdAndCourseId(studentId,courseId);
        if(studentCourse.isEmpty()){
            return List.of();
        }
        StudentCourse sc = studentCourse.get();
        List<Attendance> attendanceList = attendanceRepo.findByStudentCourseId(sc.getId());

        return attendanceList;
   }


   public Optional<StudentCourse> getStudentCourse(String studentId,String courseId){
        return studentCourseRepo.findByStudentIdAndCourseId(studentId,courseId);
   }

   public Boolean ExistsByStudentAndCourseId(String studentId,String courseId){
    return studentCourseRepo.existsByStudentIdAndCourseId(studentId,courseId);
   }
}
