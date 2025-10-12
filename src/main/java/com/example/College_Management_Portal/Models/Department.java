package com.example.College_Management_Portal.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Document(collection="Departments")
public class Department {

    @Id
    private String deptId;
    @NonNull
    private String departmentName;
}
