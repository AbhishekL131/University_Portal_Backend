package com.example.College_Management_Portal.Models;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceDto {
    private String studentId;
    private String courseId;
    private LocalDate markedOn;
    private Boolean present;
    private String remark;

    public static AttendanceDto fromEntity(Attendance attendance,StudentCourse studentCourse){
        return AttendanceDto.builder()
        .studentId(studentCourse.getStudentId())
        .courseId(studentCourse.getCourseId())
        .markedOn(attendance.getMarkedOn())
        .present(attendance.getPresent())
        .remark(attendance.getRemark())
        .build();
    }
}
