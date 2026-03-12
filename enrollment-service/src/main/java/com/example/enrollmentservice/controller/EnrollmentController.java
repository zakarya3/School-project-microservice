package com.example.enrollmentservice.controller;

import com.example.enrollmentservice.dto.EnrollmentRequestDto;
import com.example.enrollmentservice.dto.EnrollmentResponseDto;
import com.example.enrollmentservice.dto.EnrollmentUpdateDto;
import com.example.enrollmentservice.entities.EnrollmentStatus;
import com.example.enrollmentservice.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Enrollment Management", description = "APIs pour la gestion des inscriptions aux cours")
@SecurityRequirement(name = "bearerAuth")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Operation(summary = "Créer une nouvelle inscription",
               description = "Inscrire un étudiant à un cours")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inscription créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Étudiant ou cours non trouvé"),
            @ApiResponse(responseCode = "409", description = "L'étudiant est déjà inscrit à ce cours")
    })
    @PostMapping
    public ResponseEntity<EnrollmentResponseDto> createEnrollment(
            @Valid @RequestBody EnrollmentRequestDto requestDto) {
        log.info("Creating new enrollment for student {} in course {}",
                 requestDto.getStudentId(), requestDto.getCourseId());
        EnrollmentResponseDto response = enrollmentService.createEnrollment(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Récupérer toutes les inscriptions",
               description = "Obtenir la liste de toutes les inscriptions")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDto>> getAllEnrollments() {
        log.info("Fetching all enrollments");
        List<EnrollmentResponseDto> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    @Operation(summary = "Récupérer une inscription par ID",
               description = "Obtenir les détails d'une inscription spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscription trouvée"),
            @ApiResponse(responseCode = "404", description = "Inscription non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> getEnrollmentById(@PathVariable Long id) {
        log.info("Fetching enrollment with ID: {}", id);
        EnrollmentResponseDto enrollment = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(enrollment);
    }

    @Operation(summary = "Récupérer les inscriptions d'un étudiant",
               description = "Obtenir toutes les inscriptions d'un étudiant spécifique")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponseDto>> getEnrollmentsByStudent(
            @PathVariable Long studentId) {
        log.info("Fetching enrollments for student ID: {}", studentId);
        List<EnrollmentResponseDto> enrollments =
                enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @Operation(summary = "Récupérer les inscriptions d'un cours",
               description = "Obtenir toutes les inscriptions pour un cours spécifique")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponseDto>> getEnrollmentsByCourse(
            @PathVariable Long courseId) {
        log.info("Fetching enrollments for course ID: {}", courseId);
        List<EnrollmentResponseDto> enrollments =
                enrollmentService.getEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @Operation(summary = "Récupérer les inscriptions par statut",
               description = "Filtrer les inscriptions par leur statut")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EnrollmentResponseDto>> getEnrollmentsByStatus(
            @PathVariable EnrollmentStatus status) {
        log.info("Fetching enrollments with status: {}", status);
        List<EnrollmentResponseDto> enrollments =
                enrollmentService.getEnrollmentsByStatus(status);
        return ResponseEntity.ok(enrollments);
    }

    @Operation(summary = "Mettre à jour une inscription",
               description = "Modifier le statut ou les notes d'une inscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscription mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Inscription non trouvée")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> updateEnrollment(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentUpdateDto updateDto) {
        log.info("Updating enrollment with ID: {}", id);
        EnrollmentResponseDto updated = enrollmentService.updateEnrollment(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Annuler une inscription",
               description = "Changer le statut d'une inscription à CANCELLED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscription annulée avec succès"),
            @ApiResponse(responseCode = "404", description = "Inscription non trouvée")
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<EnrollmentResponseDto> cancelEnrollment(@PathVariable Long id) {
        log.info("Cancelling enrollment with ID: {}", id);
        EnrollmentResponseDto cancelled = enrollmentService.cancelEnrollment(id);
        return ResponseEntity.ok(cancelled);
    }

    @Operation(summary = "Supprimer une inscription",
               description = "Supprimer définitivement une inscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inscription supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Inscription non trouvée")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        log.info("Deleting enrollment with ID: {}", id);
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Vérifier une inscription",
               description = "Vérifier si un étudiant est inscrit à un cours")
    @ApiResponse(responseCode = "200", description = "Vérification effectuée")
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkEnrollment(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        log.info("Checking enrollment for student {} in course {}", studentId, courseId);
        boolean isEnrolled = enrollmentService.isStudentEnrolledInCourse(studentId, courseId);
        return ResponseEntity.ok(isEnrolled);
    }

    @Operation(summary = "Compter les inscriptions actives",
               description = "Obtenir le nombre d'étudiants inscrits activement à un cours")
    @ApiResponse(responseCode = "200", description = "Nombre récupéré avec succès")
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<Long> countActiveEnrollments(@PathVariable Long courseId) {
        log.info("Counting active enrollments for course ID: {}", courseId);
        Long count = enrollmentService.countActiveEnrollments(courseId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Health check", description = "Vérifier si le service est opérationnel")
    @ApiResponse(responseCode = "200", description = "Service opérationnel")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Enrollment Service is running!");
    }
}

