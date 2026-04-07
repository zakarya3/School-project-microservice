package com.example.assessmentservice.client;

import com.example.assessmentservice.config.FeignConfig;
import com.example.assessmentservice.dto.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", url = "${course-service.url}", configuration = FeignConfig.class)
public interface CourseClient {

    @GetMapping("/api/courses/{id}")
    CourseDto getCourseById(@PathVariable("id") Long id);
}
