package com.example.College_Management_Portal.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.ExamScoreCard;
import com.example.College_Management_Portal.Models.ExamScoreCardDto;
import com.example.College_Management_Portal.Models.StudentCourse;
import com.example.College_Management_Portal.Repository.ExamScoreCardRepository;
import com.example.College_Management_Portal.Repository.StudentCourseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExamScoreCardService {

    @Autowired
    private ExamScoreCardRepository examScoreCardRepo;

    @Autowired
    private StudentCourseRepository studentCourseRepo;
    

    public void createExamScoreCardForStudent(ExamScoreCardDto examScoreCardDto){
        Optional<StudentCourse> studentCourse = studentCourseRepo.findByStudentIdAndCourseId(examScoreCardDto.getStudentId(),examScoreCardDto.getCourseId());
        if(studentCourse.isPresent()){
            ExamScoreCard examScoreCard = new ExamScoreCard();
            examScoreCard.setStudentCourseId(studentCourse.get().getId());
            examScoreCard.setMaxMarks(examScoreCardDto.getMaxMarks());
            examScoreCard.setObtainedMarks(examScoreCardDto.getObtainedMarks());
            examScoreCard.setGrade(examScoreCardDto.getGrade());
            examScoreCardRepo.save(examScoreCard);
        }else{
            log.info("failed to save the score card");
        }
    }
    

    public ExamScoreCardDto getStudentExamScoreCard(String studentId,String courseId){
    Optional<StudentCourse> studentCourse = studentCourseRepo.findByStudentIdAndCourseId(studentId,courseId);
    if(studentCourse.isEmpty()){
        log.info("");
    }
    Optional<ExamScoreCard> examScoreCard = examScoreCardRepo.findByStudentCourseId(studentCourse.get().getId());
    return ExamScoreCardDto.fromEntity(examScoreCard.get(),studentCourse.get());
   }
    
}
