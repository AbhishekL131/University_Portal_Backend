package com.example.College_Management_Portal.Models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacultyDto {
    private String facultyId;
    private String facultyName;
    private String email;
    private String deptId;

    public static FacultyDto fromEntity(Faculty faculty){
        return FacultyDto.builder()
        .facultyId(faculty.getFacultyId())
        .facultyName(faculty.getFacultyName())
        .email(faculty.getEmail())
        .deptId(faculty.getDeptId())
        .build();
    }
}
