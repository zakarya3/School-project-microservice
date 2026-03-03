package com.example.studentservice.service;

import com.example.studentservice.dto.AuthDto;
import com.example.studentservice.entities.Student;
import org.keycloak.representations.AccessTokenResponse;

import java.util.List;

public interface UserService {
    void registerUser(AuthDto authDto);

    AccessTokenResponse loginUser(AuthDto authDto);

    void updateUser(String userId, Student updatedStudent);

    void deleteUser(String userId);

    List<Student> getAllUsers();

    Student getUserById(String userId);
}
