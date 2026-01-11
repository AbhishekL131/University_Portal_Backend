package com.example.College_Management_Portal.Repository;



import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.College_Management_Portal.Models.Course;


@Repository
public interface CourseRepository extends MongoRepository<Course,String>{

    Course findCourseByCourseName(String courseName);
    List<Course> findCourseByDeptId(String deptId);
    List<Course> findCourseByCourseCredits(int courseCredits);
    void deleteByDeptId(String deptId);
    List<Course> findByCourseIdIn(List<String> courseIDs);
  
}
