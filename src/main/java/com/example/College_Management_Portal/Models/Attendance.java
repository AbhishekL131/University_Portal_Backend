package com.example.College_Management_Portal.Models;

import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection="Attendance")
@NoArgsConstructor
public class Attendance {
    
    @Id
    private ObjectId id;

    @NonNull
    private ObjectId studentCourseId;

    @NonNull
    private LocalDate markedOn;

    @NonNull
    private Boolean present;

    @NonNull
    private String remark;
}
