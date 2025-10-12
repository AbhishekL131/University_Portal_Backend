package com.example.College_Management_Portal.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection="Students")
public class Student {
    
    @Id
    private String studentId;
    @NonNull
    private String name;
    @NonNull
    @Indexed(unique=true)
    private String email;
    @NonNull
    private String department;
    @NonNull
    private String phoneNo;

}
