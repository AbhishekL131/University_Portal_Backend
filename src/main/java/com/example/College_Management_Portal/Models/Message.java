package com.example.College_Management_Portal.Models;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.College_Management_Portal.Utils.MessageType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection="messages")
public class Message {

    @Id
    private ObjectId id;
    
    private String senderId;
    private String senderRole;

    private String receiverId;
    private String receiverRole;

    private String title;
    private String content;

    private MessageType type;

    private LocalDateTime createdAt;

}
