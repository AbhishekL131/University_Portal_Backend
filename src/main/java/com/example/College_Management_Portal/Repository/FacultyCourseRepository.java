package com.example.College_Management_Portal.Repository;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.College_Management_Portal.Models.*;


@Repository
public interface FacultyCourseRepository extends MongoRepository<FacultyCourse,ObjectId>{
  List<FacultyCourse> findFacultyByCourseId(String courseId);
  List<FacultyCourse> findCourseByFacultyId(String facultyId);
}
