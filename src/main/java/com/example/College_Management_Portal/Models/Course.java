package com.example.College_Management_Portal.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Document(collection="Courses")
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    
    @Id
    private String courseId;
    @NonNull
    private String courseName;
    @NonNull
    private int courseCredits;
    @NonNull
    private String deptId;
}
