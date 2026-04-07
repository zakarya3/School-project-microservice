package com.example.assessmentservice.client;

import com.example.assessmentservice.config.FeignConfig;
import com.example.assessmentservice.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service", url = "${student-service.url}", configuration = FeignConfig.class)
public interface StudentClient {

    @GetMapping("/users/{id}")
    StudentDto getStudentById(@PathVariable("id") Long id);
}
