package com.example.assessmentservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AssessmentRequestDto {

    private String title;

    private String description;

    private Long courseId;

    private Long studentId;

    private Double grade;

    private Double maxGrade;

    private LocalDate assessmentDate;
}
