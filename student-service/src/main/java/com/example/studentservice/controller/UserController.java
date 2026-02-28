package com.example.studentservice.controller;

import com.example.studentservice.dto.AuthDto;
import com.example.studentservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User Management", description = "Endpoints for user registration and login")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "User registered successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid input data")
        })
    @PostMapping("/register")
    public void registerUser(@RequestBody AuthDto authDto) {
        log.info("Registering user with username: {}", authDto.getUsername());
        userService.registerUser(authDto);
    }

    @Operation(summary = "Login a user", description = "Logs in a user with the provided credentials")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "User logged in successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid credentials")
        })
    @PostMapping("/login")
    public String loginUser(@RequestBody AuthDto authDto) {
        log.info("Logging in user with username: {}", authDto.getUsername());
        return userService.loginUser(authDto).getAccessToken();
    }
}
