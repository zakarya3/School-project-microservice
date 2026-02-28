package com.example.studentservice.service;

import com.example.studentservice.dto.AccessTokenResponse;
import com.example.studentservice.dto.AuthDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void registerUser(AuthDto authDto);

    AccessTokenResponse loginUser(AuthDto authDto);
}
