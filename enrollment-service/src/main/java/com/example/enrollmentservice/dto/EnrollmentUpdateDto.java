package com.example.enrollmentservice.dto;

import com.example.enrollmentservice.entities.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentUpdateDto {
    private EnrollmentStatus status;
    private String notes;
}

