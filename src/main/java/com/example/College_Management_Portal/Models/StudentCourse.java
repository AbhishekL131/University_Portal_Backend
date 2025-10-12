package com.example.College_Management_Portal.Models;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Data
@Document(collection="StudentCourse")
public class StudentCourse {
    
    @Id 
    private ObjectId id;
    @NonNull
    private String studentId;
    @NonNull
    private String courseId;
    @CreatedDate
    @Field("EnrolledOn")
    private LocalDateTime enrolledOn;
}
