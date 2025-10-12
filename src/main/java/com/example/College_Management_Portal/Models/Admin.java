package com.example.College_Management_Portal.Models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection="Admin")
public class Admin {
    
    @Id
    private String userName;
    @NonNull
    private String password;
    private List<String> roles;
}
