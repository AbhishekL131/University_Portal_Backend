package com.example.College_Management_Portal.Service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.Course;
import com.example.College_Management_Portal.Repository.CourseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepo;

    public void createNewCourse(Course course){
        courseRepo.save(course);
    }

    @Cacheable(value = "course",key = "#courseId")
    public Optional<Course> getCourseById(String courseId){
        log.info("reading from db for course "+courseId);
        return courseRepo.findById(courseId);
    }

  //  @Cacheable(value="coursesOfDept",key="#deptId")
    public List<Course> getByDeptId(String deptId){
        return courseRepo.findCourseByDeptId(deptId);
    }

    public List<Course> getByCredits(int courseCredits){
        return courseRepo.findCourseByCourseCredits(courseCredits);
    }
   
    public void deleteCourseByDepartmentId(String deptId){
        courseRepo.deleteByDeptId(deptId);
    }

    @Cacheable(value="courses",key="#facultyId")
    public List<Course> getAllCoursesWithIDs(String facultyId,List<String> courseIDs){
        return courseRepo.findByCourseIdIn(courseIDs);
    }

    public List<Course> getAllStudentCoursesWithIDs(String studentId,List<String> courseIDs){
        return courseRepo.findByCourseIdIn(courseIDs);
    }
}
