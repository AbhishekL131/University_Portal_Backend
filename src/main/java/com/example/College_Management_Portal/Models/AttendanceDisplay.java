package com.example.College_Management_Portal.Models;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceDisplay {
    private LocalDate markedOn;
    private Boolean present;
    private String remark;

    public static AttendanceDisplay fromEntity(Attendance attd){
        return AttendanceDisplay.builder()
        .markedOn(attd.getMarkedOn())
        .present(attd.getPresent())
        .remark(attd.getRemark())
        .build();
    }
}
