package com.example.College_Management_Portal.DTOs;

import com.example.College_Management_Portal.Utils.MessageType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageRequestDto {
    private String receiverId;
    private String title;
    private String content;
    private MessageType type;
}
