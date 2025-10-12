package com.example.College_Management_Portal.Service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.*;
import com.example.College_Management_Portal.Repository.StudentCourseRepository;

@Service
public class StudentCourseService {
    
    @Autowired
    private StudentCourseRepository studentCourseRepo;

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
}
