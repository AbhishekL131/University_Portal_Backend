package com.example.College_Management_Portal.Service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.Admin;
import com.example.College_Management_Portal.Repository.AdminRepository;

@Service
public class AdminService {
    

    @Autowired
    private AdminRepository adminRepository;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void createNewAdmin(Admin admin){
        try{
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setRoles(Arrays.asList("Admin"));
            adminRepository.save(admin);
        }catch(Exception e){

        }
    }
}
