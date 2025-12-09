package com.example.College_Management_Portal.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.FacultyCourse;
import com.example.College_Management_Portal.Repository.FacultyCourseRepository;

@Service
public class FacultyCourseService {

    @Autowired
    private FacultyCourseRepository facultyCourseRepo;

    public void createFacultyCourseEntry(FacultyCourse facultyCourse){
        facultyCourseRepo.save(facultyCourse);
    }

    public List<FacultyCourse> getAllCoursesOfFaculty(String facultyId){
        return facultyCourseRepo.findCourseByFacultyId(facultyId);
    }

    public List<FacultyCourse> getAllFacultiesOfCourse(String courseId){
        return facultyCourseRepo.findFacultyByCourseId(courseId);
    }
    
    public Boolean ExistsByFacultyAndCourseId(String facultyId,String courseId){
        return facultyCourseRepo.existsByFacultyIdAndCourseId(facultyId,courseId);
    }
}
