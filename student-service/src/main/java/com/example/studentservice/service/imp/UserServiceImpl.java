package com.example.studentservice.service.imp;

import com.example.studentservice.config.KeycloakService;
import com.example.studentservice.dto.AuthDto;
import com.example.studentservice.entities.Student;
import com.example.studentservice.repository.StudentRepository;
import com.example.studentservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StudentRepository studentRepository;
    private final KeycloakService  keycloakService;
    private final UserProducerKafka userProducerKafka;

    @Override
    @Transactional
    public void registerUser(AuthDto authDto) {
        try {
            String keycloakUserId = keycloakService.createUser(authDto.getUsername(), authDto.getPassword(), authDto.getRoleName(), authDto.getEmail(), authDto.getFirstName(), authDto.getLastName());
            Student student = new Student();
            student.setUsername(authDto.getUsername());
            student.setFirstName(authDto.getFirstName());
            student.setLastName(authDto.getLastName());
            student.setEmail(authDto.getEmail());
            student.setKeycloakId(keycloakUserId);
            student.setRoleName(authDto.getRoleName());
            studentRepository.save(student);
            userProducerKafka.sendUserRegisteredEvent(authDto);
        } catch (Exception e) {
            throw new RuntimeException("User registration failed: " + e.getMessage());
        }
    }

    @Override
    public AccessTokenResponse loginUser(AuthDto authDto) {
        try {
            return keycloakService.loginUser(authDto.getUsername(), authDto.getPassword());
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public void updateUser(String userId, Student updatedStudent) {
        Student existingStudent = studentRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingStudent.setFirstName(updatedStudent.getFirstName());
        existingStudent.setLastName(updatedStudent.getLastName());
        existingStudent.setEmail(updatedStudent.getEmail());
        studentRepository.save(existingStudent);
        keycloakService.updateUser(existingStudent.getKeycloakId(), updatedStudent.getFirstName(), updatedStudent.getLastName(), updatedStudent.getEmail());
    }

    @Override
    public void deleteUser(String userId) {
        Student existingStudent = studentRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        keycloakService.deleteUser(existingStudent.getKeycloakId());
        studentRepository.delete(existingStudent);
    }

    @Override
    public List<Student> getAllUsers() {
        return studentRepository.findAll();
    }

    @Override
    public Student getUserById(String userId) {
        return studentRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
