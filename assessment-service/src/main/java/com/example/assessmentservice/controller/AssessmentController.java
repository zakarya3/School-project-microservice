package com.example.assessmentservice.controller;

import com.example.assessmentservice.dto.AssessmentRequestDto;
import com.example.assessmentservice.dto.AssessmentResponseDto;
import com.example.assessmentservice.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments")
@RequiredArgsConstructor
@Slf4j
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PostMapping
    public ResponseEntity<AssessmentResponseDto> create(@RequestBody AssessmentRequestDto dto) {
        log.info("Creating assessment: {}", dto.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(assessmentService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<AssessmentResponseDto>> findAll() {
        return ResponseEntity.ok(assessmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssessmentResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.findById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AssessmentResponseDto>> findByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(assessmentService.findByStudentId(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AssessmentResponseDto>> findByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(assessmentService.findByCourseId(courseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssessmentResponseDto> update(@PathVariable Long id,
                                                        @RequestBody AssessmentRequestDto dto) {
        log.info("Updating assessment with id: {}", id);
        return ResponseEntity.ok(assessmentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting assessment with id: {}", id);
        assessmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
