package com.example.College_Management_Portal.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.College_Management_Portal.Models.Attendance;

public interface AttendanceRepository extends MongoRepository<Attendance,ObjectId>{
    List<Attendance> findByStudentCourseId(ObjectId studentCourseId);
}
