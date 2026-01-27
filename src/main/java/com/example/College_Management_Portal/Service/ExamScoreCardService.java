package com.example.College_Management_Portal.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;

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
    

    @Cacheable(value="ScoreCard")
    public ExamScoreCardDto getStudentExamScoreCard(String studentId,String courseId){
    Optional<StudentCourse> studentCourse = studentCourseRepo.findByStudentIdAndCourseId(studentId,courseId);
    if(studentCourse.isEmpty()){
        log.info("");
    }
    Optional<ExamScoreCard> examScoreCard = examScoreCardRepo.findByStudentCourseId(studentCourse.get().getId());
    return ExamScoreCardDto.fromEntity(examScoreCard.get(),studentCourse.get());
   }

   
   public List<ExamScoreCardDto> getAllExamScoreCards(String studentId,List<StudentCourse> studentCourses){
        Map<ObjectId,StudentCourse> studentCourseMap = studentCourses
        .stream()
        .collect(
            Collectors.toMap(
                StudentCourse::getId,
                Function.identity()
            )
        );
        List<ObjectId> studentCourseIDs = studentCourses
        .stream()
        .map(sc -> sc.getId())
        .toList();

        List<ExamScoreCard> scoreCards = examScoreCardRepo.findByStudentCourseIdIn(studentCourseIDs);
        if(scoreCards.isEmpty()){
            log.info("no score cards yet");
        }

        return scoreCards
        .stream()
        .map(
            sc -> {
                StudentCourse course = studentCourseMap.get(sc.getStudentCourseId());

                return ExamScoreCardDto.fromEntity(sc,course);
            }
        ).toList();
    }
    
}
