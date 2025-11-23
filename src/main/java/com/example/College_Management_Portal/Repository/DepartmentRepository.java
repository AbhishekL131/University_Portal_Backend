package com.example.College_Management_Portal.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.College_Management_Portal.Models.Department;


@Repository
public interface DepartmentRepository extends MongoRepository<Department,String>{

}
