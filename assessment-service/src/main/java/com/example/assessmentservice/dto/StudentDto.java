package com.example.assessmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String keycloakId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String roleName;
}
