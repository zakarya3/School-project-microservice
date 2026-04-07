package com.example.assessmentservice.repository;

import com.example.assessmentservice.entities.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    List<Assessment> findByCourseId(Long courseId);

    List<Assessment> findByStudentId(Long studentId);
}
