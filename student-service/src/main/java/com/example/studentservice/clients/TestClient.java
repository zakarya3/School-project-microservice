package com.example.studentservice.clients;

import com.example.studentservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "course-service", url = "${course-service.url}", configuration =  FeignConfig.class)
public interface TestClient {

    @GetMapping("/api/test/hello")
    String hello();
}
