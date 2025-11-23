package com.example.College_Management_Portal.Service;

import java.util.Optional;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.Department;
import com.example.College_Management_Portal.Repository.DepartmentRepository;

@Service
public class DepartmentService {
    

    @Autowired
    private DepartmentRepository deptRepo;



    public Optional<Department> getDepartmentById(String deptId){
        return deptRepo.findById(deptId);
    }

    public void createNewDepartment(Department department){
          deptRepo.save(department);
    }

    public void deleteByDepartmentId(String deptId){
        deptRepo.deleteById(deptId);
    }

    public List<Department> getAllDepartments(){
        return deptRepo.findAll();
    }
}
