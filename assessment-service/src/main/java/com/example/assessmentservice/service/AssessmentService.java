package com.example.assessmentservice.service;

import com.example.assessmentservice.dto.AssessmentRequestDto;
import com.example.assessmentservice.dto.AssessmentResponseDto;

import java.util.List;

public interface AssessmentService {

    AssessmentResponseDto create(AssessmentRequestDto dto);

    AssessmentResponseDto findById(Long id);

    List<AssessmentResponseDto> findAll();

    List<AssessmentResponseDto> findByStudentId(Long studentId);

    List<AssessmentResponseDto> findByCourseId(Long courseId);

    AssessmentResponseDto update(Long id, AssessmentRequestDto dto);

    void delete(Long id);
}
