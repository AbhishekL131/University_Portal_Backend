package com.example.College_Management_Portal.DemoTest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.College_Management_Portal.Utils.JwtUtil;

@Component
public class JwtAuth {

    @Autowired
    private JwtUtil jwtUtil;

    public void getAllJwtDetails(String token){
        String userName = jwtUtil.extractUsername(token);
        System.out.println("userName : "+userName);
        List<String> roles = jwtUtil.extractRoles(token);
        System.out.println("roles : "+roles);
        System.out.println("claims : "+jwtUtil.extractAllClaims(token));
    }
}
