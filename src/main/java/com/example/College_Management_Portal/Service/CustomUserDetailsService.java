package com.example.College_Management_Portal.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.Admin;
import com.example.College_Management_Portal.Models.Faculty;
import com.example.College_Management_Portal.Repository.AdminRepository;
import com.example.College_Management_Portal.Repository.FacultyRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        
        Optional<Faculty> facultyOpt = facultyRepository.findFacultyByUserName(userName);
        if (facultyOpt.isPresent()) {
            Faculty faculty = facultyOpt.get();
            if (faculty.getRoles() == null || faculty.getRoles().isEmpty()) {
                log.warn("Faculty {} has no roles assigned", userName);
                throw new UsernameNotFoundException("Faculty has no roles assigned: " + userName);
            }
            return buildUserDetails(
                faculty.getUserName(),
                faculty.getPassword(),
                faculty.getRoles()
            );
        }

      
        Optional<Admin> adminOpt = adminRepository.findByUserName(userName);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (admin.getRoles() == null || admin.getRoles().isEmpty()) {
                log.warn("Admin {} has no roles assigned", userName);
                throw new UsernameNotFoundException("Admin has no roles assigned: " + userName);
            }
            return buildUserDetails(
                admin.getUserName(),
                admin.getPassword(),
                admin.getRoles()
            );
        }

       
        log.warn("User not found: {}", userName);
        throw new UsernameNotFoundException("User not found: " + userName);
    }

    private UserDetails buildUserDetails(String username, String password, List<String> roles) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(password)
                .roles(roles.toArray(new String[0]))
                .build();
    }
}
