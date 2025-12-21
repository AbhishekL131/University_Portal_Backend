package com.example.College_Management_Portal.Repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.College_Management_Portal.Models.ExamScoreCard;

public interface ExamScoreCardRepository extends MongoRepository<ExamScoreCard,ObjectId>{
    Optional<ExamScoreCard> findByStudentCourseId(ObjectId studentCourseId);
}
