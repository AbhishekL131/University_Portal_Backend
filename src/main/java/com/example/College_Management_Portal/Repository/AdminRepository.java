package com.example.College_Management_Portal.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.College_Management_Portal.Models.Admin;


@Repository
public interface AdminRepository extends MongoRepository<Admin,String>{
    Optional<Admin> findByUserName(String userName);
}
