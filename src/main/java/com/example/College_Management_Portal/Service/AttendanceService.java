package com.example.College_Management_Portal.Service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.Models.Attendance;
import com.example.College_Management_Portal.Models.AttendanceDto;
import com.example.College_Management_Portal.Models.StudentCourse;
import com.example.College_Management_Portal.Repository.AttendanceRepository;
import com.example.College_Management_Portal.Repository.StudentCourseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceService {
    
    @Autowired
    private StudentCourseRepository studentCourseRepo;

    @Autowired
    private AttendanceRepository attendanceRepo;

    public void saveAttendance(AttendanceDto atDto){
        Optional<StudentCourse> studentCourse = studentCourseRepo.findByStudentIdAndCourseId(atDto.getStudentId(),atDto.getCourseId());
        if(studentCourse.isPresent()){
            Attendance attd = new Attendance();
            attd.setStudentCourseId(studentCourse.get().getId());
            attd.setMarkedOn(atDto.getMarkedOn());
            attd.setPresent(atDto.getPresent());
            attd.setRemark(atDto.getRemark());
            attendanceRepo.save(attd);
        }else{
            log.info("failed to save the attendance");
        }
    }

    public List<Attendance> getByStudentCourseId(ObjectId studentCourseId){
        return attendanceRepo.findByStudentCourseId(studentCourseId);
    }
}
