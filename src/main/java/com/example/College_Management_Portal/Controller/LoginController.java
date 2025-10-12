package com.example.College_Management_Portal.Controller;

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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/Login")
@Slf4j
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
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new ResponseEntity<>(jwt,HttpStatus.OK);
       }catch(Exception e){
        log.info("error");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }


     @PostMapping("/adminLogin")
    public ResponseEntity<?> login(@RequestBody Admin admin){
       try{
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(admin.getUserName(),admin.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(admin.getUserName());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new ResponseEntity<>(jwt,HttpStatus.OK);
       }catch(Exception e){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }


}
