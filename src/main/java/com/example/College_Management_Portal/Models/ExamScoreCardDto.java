package com.example.College_Management_Portal.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
