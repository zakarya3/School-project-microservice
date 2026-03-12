package com.example.enrollmentservice.service;

import com.example.enrollmentservice.dto.EnrollmentRequestDto;
import com.example.enrollmentservice.dto.EnrollmentResponseDto;
import com.example.enrollmentservice.dto.EnrollmentUpdateDto;
import com.example.enrollmentservice.entities.EnrollmentStatus;

import java.util.List;

public interface EnrollmentService {

    // Créer une nouvelle inscription
    EnrollmentResponseDto createEnrollment(EnrollmentRequestDto requestDto);

    // Récupérer toutes les inscriptions
    List<EnrollmentResponseDto> getAllEnrollments();

    // Récupérer une inscription par ID
    EnrollmentResponseDto getEnrollmentById(Long id);

    // Récupérer toutes les inscriptions d'un étudiant
    List<EnrollmentResponseDto> getEnrollmentsByStudentId(Long studentId);

    // Récupérer toutes les inscriptions à un cours
    List<EnrollmentResponseDto> getEnrollmentsByCourseId(Long courseId);

    // Récupérer les inscriptions par statut
    List<EnrollmentResponseDto> getEnrollmentsByStatus(EnrollmentStatus status);

    // Mettre à jour une inscription
    EnrollmentResponseDto updateEnrollment(Long id, EnrollmentUpdateDto updateDto);

    // Supprimer une inscription
    void deleteEnrollment(Long id);

    // Annuler une inscription
    EnrollmentResponseDto cancelEnrollment(Long id);

    // Vérifier si un étudiant est inscrit à un cours
    boolean isStudentEnrolledInCourse(Long studentId, Long courseId);

    // Compter les inscriptions actives pour un cours
    Long countActiveEnrollments(Long courseId);
}

