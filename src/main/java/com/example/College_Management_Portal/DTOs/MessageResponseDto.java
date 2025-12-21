package com.example.College_Management_Portal.DTOs;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponseDto {
    private String title;
    private String content;
    private String senderId;
    private LocalDateTime createdAt;
}
