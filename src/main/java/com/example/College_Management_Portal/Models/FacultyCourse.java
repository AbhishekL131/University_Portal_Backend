package com.example.College_Management_Portal.Models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection="FacultyCourse")
@AllArgsConstructor
@NoArgsConstructor
public class FacultyCourse {
    
    @Id
    private ObjectId id;
    @NonNull
    private String facultyId;
    @NonNull
    private String courseId;
}
