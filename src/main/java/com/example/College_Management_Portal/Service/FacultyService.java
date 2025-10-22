package com.example.College_Management_Portal.Service;

import java.util.*;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.Faculty;
import com.example.College_Management_Portal.Repository.FacultyRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FacultyService {
    
    @Autowired
    private FacultyRepository facultyRepo;


    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void createNewFaculty(Faculty faculty){
       try{
        faculty.setPassword(passwordEncoder.encode(faculty.getPassword()));
        faculty.setRoles(Arrays.asList("Faculty"));
        facultyRepo.save(faculty);
       }catch(Exception e){
        log.info("hey");
       }
    }

    public void createDepartmentHod(Faculty faculty){
        try{
            faculty.setPassword(passwordEncoder.encode(faculty.getPassword()));
            faculty.setRoles(Arrays.asList("Faculty","HOD"));
            facultyRepo.save(faculty);
        }catch(Exception e){

        }
    }

    public boolean departmentHasHOD(String deptId){
        return facultyRepo.existsByDeptIdAndRolesContaining(deptId,"HOD");
    }

    
    public List<Faculty> getAllFaculties(){
        return facultyRepo.findAll();
    }
    
    @Cacheable(key = "faculty",value = "#facultyId")
    public Optional<Faculty> getFacultyById(String facultyId){
        return facultyRepo.findById(facultyId);
    }


    
    public List<Faculty> getAllByDeptId(String deptId){
        return facultyRepo.findAllByDeptId(deptId);
    }

    public boolean checkIfExists(String facultyId){
        return facultyRepo.existsByFacultyId(facultyId);
    }

    public Optional<Faculty> getFacultyByUserName(String username){
        return facultyRepo.findFacultyByUserName(username);
    }



    public void deleteFacultyByDepartmentId(String deptId){
        facultyRepo.deleteByDeptId(deptId);
    }
}
