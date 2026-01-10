package com.example.College_Management_Portal.Repository;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.College_Management_Portal.Models.*;

@Repository
public interface StudentCourseRepository extends MongoRepository<StudentCourse,ObjectId>{
  
    List<StudentCourse> findCourseByStudentId(String studentId);
    List<StudentCourse> findStudentByCourseId(String courseId);
    boolean existsByStudentIdAndCourseId(String studentId,String courseId);
    void deleteByStudentId(String studentId);
    Optional<StudentCourse> findByStudentIdAndCourseId(String studentId,String courseId);
    List<StudentCourse> findByCourseIdIn(List<String> courseIds);
    
}
