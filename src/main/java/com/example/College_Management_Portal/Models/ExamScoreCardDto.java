package com.example.College_Management_Portal.Models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExamScoreCardDto {
    
    private String studentId;
    private String courseId;
    private int maxMarks;
    private int obtainedMarks;
    private String grade;

    public static ExamScoreCardDto fromEntity(ExamScoreCard examScoreCard,StudentCourse studentCourse){
        return ExamScoreCardDto.builder()
        .studentId(studentCourse.getStudentId())
        .courseId(studentCourse.getCourseId())
        .maxMarks(examScoreCard.getMaxMarks())
        .obtainedMarks(examScoreCard.getObtainedMarks())
        .grade(examScoreCard.getGrade())
        .build();
    }
}
