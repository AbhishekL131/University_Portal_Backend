package com.example.College_Management_Portal.Service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.College_Management_Portal.Models.Course;

@SpringBootTest
public class CourseTest {
    

    @Autowired
    private CourseService courseService;


    @ParameterizedTest
    @CsvSource({
        "CSE",
        "ECE",
        "MECH"
    })
    public void getAllCoursesOfDept(String deptId){
        List<Course> courses = courseService.getByDeptId(deptId);
        for(Course cs : courses){
            System.out.println(cs);
        }
    }

    
}
