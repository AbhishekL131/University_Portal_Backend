package com.example.College_Management_Portal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.College_Management_Portal.Models.Admin;
import com.example.College_Management_Portal.Models.Faculty;
import com.example.College_Management_Portal.Service.CustomUserDetailsService;
import com.example.College_Management_Portal.Utils.JwtUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/Login")
@Slf4j
@Tag(name = "Login APIs")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/facultyLogin")
    public ResponseEntity<?> login(@RequestBody Faculty faculty){
        try{
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(faculty.getUserName(),faculty.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(faculty.getUserName());

        boolean hasFacultyRole = userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_Faculty") || auth.getAuthority().equals("ROLE_HOD"));
        
        if (!hasFacultyRole) {
            log.warn("Non-faculty user attempted faculty login: {}", faculty.getUserName());
            return new ResponseEntity<>("Invalid credentials",HttpStatus.BAD_REQUEST);
        }
        
        List<String> roles = userDetails.getAuthorities().stream()
            .map(auth -> auth.getAuthority().replace("ROLE_", ""))
            .toList();
        String jwt = jwtUtil.generateToken(userDetails.getUsername(), roles);
        return new ResponseEntity<>(jwt,HttpStatus.OK);
       }catch(Exception e){
        log.info("Faculty login failed: {}", e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }


     @PostMapping("/adminLogin")
    public ResponseEntity<?> login(@RequestBody Admin admin){
       try{
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(admin.getUserName(),admin.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(admin.getUserName());

      //  System.out.println("userDetails : "+userDetails);
        
        boolean hasAdminRole = userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_Admin"));

      //  List<?> rols = userDetails.getAuthorities().stream().toList();

     //   System.out.println("rols : "+rols);
        
        if (!hasAdminRole) {
            log.warn("Non-admin user attempted admin login: {}", admin.getUserName());
            return new ResponseEntity<>("Invalid credentials",HttpStatus.BAD_REQUEST);
        }
        
        List<String> roles = userDetails.getAuthorities().stream()
            .map(auth -> auth.getAuthority().replace("ROLE_", ""))
            .toList();
        String jwt = jwtUtil.generateToken(userDetails.getUsername(), roles);
        return new ResponseEntity<>(jwt,HttpStatus.OK);
       }catch(Exception e){
        log.warn("Admin login failed: {}", e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }


}
