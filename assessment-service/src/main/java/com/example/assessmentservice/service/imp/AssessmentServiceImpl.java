package com.example.assessmentservice.service.imp;

import com.example.assessmentservice.client.CourseClient;
import com.example.assessmentservice.client.StudentClient;
import com.example.assessmentservice.dto.AssessmentRequestDto;
import com.example.assessmentservice.dto.AssessmentResponseDto;
import com.example.assessmentservice.dto.CourseDto;
import com.example.assessmentservice.dto.StudentDto;
import com.example.assessmentservice.entities.Assessment;
import com.example.assessmentservice.exception.ResourceNotFoundException;
import com.example.assessmentservice.repository.AssessmentRepository;
import com.example.assessmentservice.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final StudentClient studentClient;
    private final CourseClient courseClient;

    @Override
    @Transactional
    public AssessmentResponseDto create(AssessmentRequestDto dto) {
        log.info("Creating assessment for student {} in course {}", dto.getStudentId(), dto.getCourseId());

        StudentDto student = getStudentOrThrow(dto.getStudentId());
        CourseDto course = getCourseOrThrow(dto.getCourseId());

        Assessment assessment = Assessment.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .courseId(dto.getCourseId())
                .studentId(dto.getStudentId())
                .grade(dto.getGrade())
                .maxGrade(dto.getMaxGrade())
                .assessmentDate(dto.getAssessmentDate())
                .build();

        Assessment saved = assessmentRepository.save(assessment);
        log.info("Assessment created with ID: {}", saved.getId());
        return toDto(saved, student, course);
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResponseDto findById(Long id) {
        log.info("Fetching assessment with ID: {}", id);
        Assessment assessment = findOrThrow(id);
        return toDto(assessment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponseDto> findAll() {
        log.info("Fetching all assessments");
        return assessmentRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponseDto> findByStudentId(Long studentId) {
        log.info("Fetching assessments for student ID: {}", studentId);
        getStudentOrThrow(studentId);
        return assessmentRepository.findByStudentId(studentId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponseDto> findByCourseId(Long courseId) {
        log.info("Fetching assessments for course ID: {}", courseId);
        getCourseOrThrow(courseId);
        return assessmentRepository.findByCourseId(courseId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssessmentResponseDto update(Long id, AssessmentRequestDto dto) {
        log.info("Updating assessment with ID: {}", id);
        Assessment assessment = findOrThrow(id);

        StudentDto student = getStudentOrThrow(dto.getStudentId());
        CourseDto course = getCourseOrThrow(dto.getCourseId());

        assessment.setTitle(dto.getTitle());
        assessment.setDescription(dto.getDescription());
        assessment.setCourseId(dto.getCourseId());
        assessment.setStudentId(dto.getStudentId());
        assessment.setGrade(dto.getGrade());
        assessment.setMaxGrade(dto.getMaxGrade());
        assessment.setAssessmentDate(dto.getAssessmentDate());

        return toDto(assessmentRepository.save(assessment), student, course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting assessment with ID: {}", id);
        findOrThrow(id);
        assessmentRepository.deleteById(id);
    }

    // ========== MÉTHODES UTILITAIRES ==========

    private Assessment findOrThrow(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with ID: " + id));
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

    private AssessmentResponseDto toDto(Assessment assessment) {
        StudentDto student = null;
        CourseDto course = null;

        try {
            student = studentClient.getStudentById(assessment.getStudentId());
        } catch (Exception e) {
            log.warn("Could not fetch student details for ID: {}", assessment.getStudentId());
        }

        try {
            course = courseClient.getCourseById(assessment.getCourseId());
        } catch (Exception e) {
            log.warn("Could not fetch course details for ID: {}", assessment.getCourseId());
        }

        return toDto(assessment, student, course);
    }

    private AssessmentResponseDto toDto(Assessment assessment, StudentDto student, CourseDto course) {
        return AssessmentResponseDto.builder()
                .id(assessment.getId())
                .title(assessment.getTitle())
                .description(assessment.getDescription())
                .courseId(assessment.getCourseId())
                .courseName(course != null ? course.getNomCours() : "Unknown")
                .studentId(assessment.getStudentId())
                .studentName(student != null ? student.getFirstName() + " " + student.getLastName() : "Unknown")
                .grade(assessment.getGrade())
                .maxGrade(assessment.getMaxGrade())
                .assessmentDate(assessment.getAssessmentDate())
                .build();
    }
}
