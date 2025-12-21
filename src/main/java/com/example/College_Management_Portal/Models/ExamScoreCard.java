package com.example.College_Management_Portal.Models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection="ExamScoreCard")
@NoArgsConstructor
public class ExamScoreCard {
    
    @Id
    private ObjectId id;

    @NonNull
    private ObjectId studentCourseId;

    @NonNull
    private int maxMarks;

    @NonNull
    private int obtainedMarks;

    @NonNull
    private String grade;

}
