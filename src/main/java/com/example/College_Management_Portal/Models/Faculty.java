package com.example.College_Management_Portal.Models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Document(collection="Faculties")
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {

    @Id
    private String facultyId;
    @NonNull
    private String userName;
    @NonNull
    private String password;
    @NonNull
    private String facultyName;
    @NonNull
    @Indexed(unique=true)
    private String email;
    @NonNull
    private String deptId;
    private List<String> roles;
    @CreatedDate
    @Field("JoinedOn")
    private LocalDateTime dateOfJoining;
    
}
