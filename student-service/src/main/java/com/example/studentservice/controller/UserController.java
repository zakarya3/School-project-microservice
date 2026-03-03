package com.example.studentservice.controller;

import com.example.studentservice.dto.AuthDto;
import com.example.studentservice.entities.Student;
import com.example.studentservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.keycloak.representations.AccessTokenResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AccessTokenResponse> loginUser(@RequestBody AuthDto authDto) {
        AccessTokenResponse res = userService.loginUser(authDto);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Update user details", description = "Updates the details of an existing user")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "User updated successfully"),
                @ApiResponse(responseCode = "404", description = "User not found")
        })
    @PutMapping("/{userId}")
    public void updateUser(@PathVariable String userId, @RequestBody Student user) {
        userService.updateUser(userId, user);
    }

    @Operation(summary = "Delete a user", description = "Deletes an existing user by their ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "User deleted successfully"),
                @ApiResponse(responseCode = "404", description = "User not found")
        })
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }

        @Operation(summary = "Get all users", description = "Retrieves a list of all registered users")
            @ApiResponses(value = {
                    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
            })
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
