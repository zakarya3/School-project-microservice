package com.example.courseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String keycloakUserId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String roleName;
}
