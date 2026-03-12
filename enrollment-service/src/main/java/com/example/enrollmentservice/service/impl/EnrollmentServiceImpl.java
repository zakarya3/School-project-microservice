package com.example.enrollmentservice.service.impl;

import com.example.enrollmentservice.client.CourseClient;
import com.example.enrollmentservice.client.StudentClient;
import com.example.enrollmentservice.dto.*;
import com.example.enrollmentservice.entities.Enrollment;
import com.example.enrollmentservice.entities.EnrollmentStatus;
import com.example.enrollmentservice.exception.DuplicateEnrollmentException;
import com.example.enrollmentservice.exception.ResourceNotFoundException;
import com.example.enrollmentservice.repository.EnrollmentRepository;
import com.example.enrollmentservice.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentClient studentClient;
    private final CourseClient courseClient;

    @Override
    @Transactional
    public EnrollmentResponseDto createEnrollment(EnrollmentRequestDto requestDto) {
        log.info("Creating enrollment for student {} in course {}",
                 requestDto.getStudentId(), requestDto.getCourseId());

        // Vérifier si l'étudiant existe
        StudentDto student = getStudentOrThrow(requestDto.getStudentId());

        // Vérifier si le cours existe
        CourseDto course = getCourseOrThrow(requestDto.getCourseId());

        // Vérifier si l'étudiant est déjà inscrit
        if (enrollmentRepository.existsByStudentIdAndCourseId(
                requestDto.getStudentId(), requestDto.getCourseId())) {
            throw new DuplicateEnrollmentException(
                    "Student is already enrolled in this course");
        }

        // Créer l'inscription
        Enrollment enrollment = Enrollment.builder()
                .studentId(requestDto.getStudentId())
                .courseId(requestDto.getCourseId())
                .status(EnrollmentStatus.ACTIVE)
                .notes(requestDto.getNotes())
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        log.info("Enrollment created successfully with ID: {}", savedEnrollment.getId());

        return mapToResponseDto(savedEnrollment, student, course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDto> getAllEnrollments() {
        log.info("Fetching all enrollments");
        return enrollmentRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponseDto getEnrollmentById(Long id) {
        log.info("Fetching enrollment with ID: {}", id);
        Enrollment enrollment = findEnrollmentOrThrow(id);
        return mapToResponseDto(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDto> getEnrollmentsByStudentId(Long studentId) {
        log.info("Fetching enrollments for student ID: {}", studentId);

        // Vérifier que l'étudiant existe
        getStudentOrThrow(studentId);

        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDto> getEnrollmentsByCourseId(Long courseId) {
        log.info("Fetching enrollments for course ID: {}", courseId);

        // Vérifier que le cours existe
        getCourseOrThrow(courseId);

        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDto> getEnrollmentsByStatus(EnrollmentStatus status) {
        log.info("Fetching enrollments with status: {}", status);
        return enrollmentRepository.findByStatus(status).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EnrollmentResponseDto updateEnrollment(Long id, EnrollmentUpdateDto updateDto) {
        log.info("Updating enrollment with ID: {}", id);

        Enrollment enrollment = findEnrollmentOrThrow(id);

        if (updateDto.getStatus() != null) {
            enrollment.setStatus(updateDto.getStatus());
        }
        if (updateDto.getNotes() != null) {
            enrollment.setNotes(updateDto.getNotes());
        }

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        log.info("Enrollment updated successfully");

        return mapToResponseDto(updatedEnrollment);
    }

    @Override
    @Transactional
    public void deleteEnrollment(Long id) {
        log.info("Deleting enrollment with ID: {}", id);

        Enrollment enrollment = findEnrollmentOrThrow(id);
        enrollmentRepository.delete(enrollment);

        log.info("Enrollment deleted successfully");
    }

    @Override
    @Transactional
    public EnrollmentResponseDto cancelEnrollment(Long id) {
        log.info("Cancelling enrollment with ID: {}", id);

        Enrollment enrollment = findEnrollmentOrThrow(id);
        enrollment.setStatus(EnrollmentStatus.CANCELLED);

        Enrollment cancelledEnrollment = enrollmentRepository.save(enrollment);
        log.info("Enrollment cancelled successfully");

        return mapToResponseDto(cancelledEnrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEnrolledInCourse(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countActiveEnrollments(Long courseId) {
        return enrollmentRepository.countActiveEnrollmentsByCourseId(courseId);
    }

    // ========== MÉTHODES UTILITAIRES ==========

    private Enrollment findEnrollmentOrThrow(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found with ID: " + id));
    }

    private StudentDto getStudentOrThrow(Long studentId) {
        try {
            return studentClient.getStudentById(studentId);
        } catch (Exception e) {
            log.error("Error fetching student with ID: {}", studentId, e);
            throw new ResourceNotFoundException("Student not found with ID: " + studentId);
        }
    }

    private CourseDto getCourseOrThrow(Long courseId) {
        try {
            return courseClient.getCourseById(courseId);
        } catch (Exception e) {
            log.error("Error fetching course with ID: {}", courseId, e);
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
        }
    }

    private EnrollmentResponseDto mapToResponseDto(Enrollment enrollment) {
        StudentDto student = null;
        CourseDto course = null;

        try {
            student = studentClient.getStudentById(enrollment.getStudentId());
        } catch (Exception e) {
            log.warn("Could not fetch student details for ID: {}", enrollment.getStudentId());
        }

        try {
            course = courseClient.getCourseById(enrollment.getCourseId());
        } catch (Exception e) {
            log.warn("Could not fetch course details for ID: {}", enrollment.getCourseId());
        }

        return mapToResponseDto(enrollment, student, course);
    }

    private EnrollmentResponseDto mapToResponseDto(Enrollment enrollment,
                                                   StudentDto student,
                                                   CourseDto course) {
        return EnrollmentResponseDto.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .studentName(student != null ?
                        student.getFirstName() + " " + student.getLastName() : "Unknown")
                .courseId(enrollment.getCourseId())
                .courseName(course != null ? course.getNomCours() : "Unknown")
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus())
                .notes(enrollment.getNotes())
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                .build();
    }
}

