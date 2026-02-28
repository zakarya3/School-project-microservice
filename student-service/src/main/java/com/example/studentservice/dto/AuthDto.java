package com.example.studentservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDto {
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String email;
    public String roleName;
}
