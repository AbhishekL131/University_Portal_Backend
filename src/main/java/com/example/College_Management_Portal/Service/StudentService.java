package com.example.College_Management_Portal.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.Student;
import com.example.College_Management_Portal.Repository.StudentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentService {

    @Autowired
    private StudentRepository studentRepo;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void saveStudent(Student student){
        try{
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            student.setRoles(Arrays.asList("Student"));
            studentRepo.save(student);
        }catch(Exception e){
            log.info("error occured while storing student data");
        }
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

    public Optional<Student> getStudentByUserName(String username){
        return studentRepo.findStudentByUserName(username);
    }
    
    public List<Student> getStudentsByIDs(List<String> studentIDs){
        return studentRepo.findAllById(studentIDs);
    }
}
