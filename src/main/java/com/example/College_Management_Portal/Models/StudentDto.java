package com.example.College_Management_Portal.Models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDto {
    private String studentId;
    private String name;
    private String email;
    private String department;
    private String phoneNo;

    public static StudentDto fromEntity(Student student){
        return StudentDto.builder()
        .studentId(student.getStudentId())
        .name(student.getName())
        .email(student.getEmail())
        .department(student.getDepartment())
        .phoneNo(student.getPhoneNo())
        .build();
    }
}
