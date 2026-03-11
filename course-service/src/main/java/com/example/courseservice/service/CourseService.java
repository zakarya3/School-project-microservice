package com.example.courseservice.service;

import com.example.courseservice.entities.Course;
import com.example.courseservice.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course course) {
        Course existing = courseRepository.findById(id).orElse(null);

        if (existing != null) {
            existing.setNomCours(course.getNomCours());
            existing.setCodeCours(course.getCodeCours());
            existing.setDescription(course.getDescription());
            existing.setVolumeHoraire(course.getVolumeHoraire());
            existing.setSemestre(course.getSemestre());
            existing.setAnneeScolaire(course.getAnneeScolaire());
            existing.setStatut(course.getStatut());

            return courseRepository.save(existing);
        }

        return null;
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
