package com.example.College_Management_Portal.Models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection="StudentCourse")
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {
    
    @Id 
    private ObjectId id;
    @NonNull
    private String studentId;
    @NonNull
    private String courseId;
}
