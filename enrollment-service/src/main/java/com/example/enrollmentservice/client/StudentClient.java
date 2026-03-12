package com.example.enrollmentservice.client;

import com.example.enrollmentservice.config.FeignConfig;
import com.example.enrollmentservice.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service", url = "${student-service.url}", configuration = FeignConfig.class)
public interface StudentClient {

    @GetMapping("/users/{id}")
    StudentDto getStudentById(@PathVariable("id") Long id);
}

