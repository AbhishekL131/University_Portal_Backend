package com.example.College_Management_Portal.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.College_Management_Portal.DTOs.MessageRequestDto;
import com.example.College_Management_Portal.Models.Message;
import com.example.College_Management_Portal.Repository.MessageRepository;
import com.example.College_Management_Portal.Utils.MessageType;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepo;

    public void sendMessage(String facultyId,MessageRequestDto dto){
        
        Message message = Message.builder()
        .senderId(facultyId)
        .senderRole("FACULTY")
        .receiverId(dto.getType() == MessageType.DIRECT ? dto.getReceiverId() : null)
        .receiverRole(dto.getType() == MessageType.DIRECT ? "STUDENT" : "ALL")
        .title(dto.getTitle())
        .content(dto.getContent())
        .type(dto.getType())
        .createdAt(LocalDateTime.now())
        .build();

        messageRepo.save(message);
    }

    public void sendMessageFromDepartment(String deptId,MessageRequestDto dto){

        dto.setType(MessageType.ANNOUNCEMENT);
        Message message = Message.builder()
        .senderId(deptId)
        .senderRole("DEPARTMENT")
        .receiverId(null)
        .receiverRole("FACULTY")
        .title(dto.getTitle())
        .content(dto.getContent())
        .type(dto.getType())
        .createdAt(LocalDateTime.now())
        .build();

        messageRepo.save(message);
    }

    public List<Message> getAllMessagesOfReceiver(String receiverId){
        List<Message> messages = new ArrayList<>();
        messages.addAll(messageRepo.findByReceiverId(receiverId));
        messages.addAll(messageRepo.findByType(MessageType.ANNOUNCEMENT));
        return messages;
    }

    

    
}
