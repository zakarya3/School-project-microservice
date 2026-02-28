package com.example.studentservice.service.imp;

import com.example.studentservice.config.KeycloakService;
import com.example.studentservice.dto.AccessTokenResponse;
import com.example.studentservice.dto.AuthDto;
import com.example.studentservice.entities.Student;
import com.example.studentservice.repository.StudentRepository;
import com.example.studentservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StudentRepository studentRepository;
    private final KeycloakService  keycloakService;

    @Override
    public void registerUser(AuthDto authDto) {
        String keycloakUserId = keycloakService.createUser(authDto.getUsername(), authDto.getPassword(), authDto.getRoleName());
        Student student = new Student();
        student.setUsername(authDto.getUsername());
        student.setFirstName(authDto.getFirstName());
        student.setLastName(authDto.getLastName());
        student.setEmail(authDto.getEmail());
        student.setKeycloakId(keycloakUserId);
        student.setRoleName(authDto.getRoleName());
        studentRepository.save(student);
    }

    @Override
    public AccessTokenResponse loginUser(AuthDto authDto) {
        String accessToken = keycloakService.loginUser(authDto.getUsername(), authDto.getPassword());
        return new AccessTokenResponse(accessToken);
    }
}
