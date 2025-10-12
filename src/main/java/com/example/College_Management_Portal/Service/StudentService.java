package com.example.College_Management_Portal.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.Student;
import com.example.College_Management_Portal.Repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepo;

    public void saveStudent(Student student){
        studentRepo.save(student);
    }

    public Optional<Student> getStudentById(String studentId){
        return studentRepo.findById(studentId);
    }

    public List<Student> allStudents(){
        return studentRepo.findAll();
    }

    public List<Student> getAllStudentsOfDepartment(String deptId){
        return studentRepo.findStudentByDepartment(deptId);
    }
    public void deleteStudentById(String studentId){
        studentRepo.deleteById(studentId);
    }

    public void deleteStudentByDepartmentId(String deptId){
        studentRepo.deleteByDepartment(deptId);
    }
    
}
