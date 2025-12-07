package com.example.College_Management_Portal.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.College_Management_Portal.Filter.JwtFilter;



@Configuration
@EnableWebSecurity
public class SpringSecurity {
    
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/Login/**").permitAll()
            .requestMatchers("/swagger-ui/**","/v3/api-docs/**","/swagger-ui.html").permitAll()
            .requestMatchers("/api/test-cache/**").permitAll()
            .requestMatchers("/Admin/**").hasRole("Admin")
            .requestMatchers("/department/**").hasRole("Admin")
            .requestMatchers("/Course/**").hasAnyRole("Faculty","HOD","Admin")
            .requestMatchers("/Faculty/**").hasAnyRole("Faculty","HOD")
            .requestMatchers("/Student/**").hasAnyRole("Faculty","HOD","Admin")
            .requestMatchers("/facultyStudent/**").hasAnyRole("Faculty","HOD")
            .requestMatchers("/studentCourse/**").hasAnyRole("Faculty","HOD","Admin")
            .requestMatchers("/facultyCourse/**").hasAnyRole("Faculty","HOD","Admin")
            .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception{
        return auth.getAuthenticationManager();
    }
    
    
}
