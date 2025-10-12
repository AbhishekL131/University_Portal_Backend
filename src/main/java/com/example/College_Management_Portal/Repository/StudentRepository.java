package com.example.College_Management_Portal.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.College_Management_Portal.Models.Student;



@Repository
public interface StudentRepository extends MongoRepository<Student,String>{
    
    List<Student> findStudentByDepartment(String department);
    void deleteByDepartment(String department);
}
