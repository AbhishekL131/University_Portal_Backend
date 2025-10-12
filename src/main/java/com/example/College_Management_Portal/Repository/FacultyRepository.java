package com.example.College_Management_Portal.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.College_Management_Portal.Models.Faculty;


@Repository
public interface FacultyRepository extends MongoRepository<Faculty,String>{
  List<Faculty> findAllByDeptId(String deptId);
  boolean existsByFacultyId(String facultyId);
  Optional<Faculty> findFacultyByUserName(String userName);
  void deleteByDeptId(String deptId);
  boolean existsByDeptIdAndRolesContaining(String deptId,String role);
}
