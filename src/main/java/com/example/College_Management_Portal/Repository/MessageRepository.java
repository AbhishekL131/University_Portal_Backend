package com.example.College_Management_Portal.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.College_Management_Portal.Models.Message;
import com.example.College_Management_Portal.Utils.MessageType;

public interface MessageRepository extends MongoRepository<Message,ObjectId>{

    List<Message> findByReceiverId(String receiverId);
    List<Message> findByType(MessageType type);

}
