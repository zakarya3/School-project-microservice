package com.example.assessmentservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AssessmentResponseDto {

    private Long id;

    private String title;

    private String description;

    private Long courseId;

    private String courseName;

    private Long studentId;

    private String studentName;

    private Double grade;

    private Double maxGrade;

    private LocalDate assessmentDate;
}
