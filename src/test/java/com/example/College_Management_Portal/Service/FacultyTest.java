package com.example.College_Management_Portal.Service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.example.College_Management_Portal.Models.Faculty;
import com.example.College_Management_Portal.Repository.FacultyRepository;

@SpringBootTest
public class FacultyTest {
    
    @Autowired
    private FacultyRepository facultyRepo;

   // private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testing(){
        Optional<Faculty> faculty = facultyRepo.findFacultyByUserName("CSEF001.iiitn");
        System.out.println(faculty.get().getFacultyName());
    }

    @Test
    public void Test2(){
        Optional<Faculty> faculty = facultyRepo.findFacultyByUserName("CSEF001.iiitn");
        System.out.println("faculty name : "+faculty.get().getFacultyName());
        System.out.println("Password : "+faculty.get().getPassword());
    }
}
