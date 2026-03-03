package com.example.studentservice.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor bearerTokenInterceptor() {
        return requestTemplate -> {

            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String token = request.getHeader(HttpHeaders.AUTHORIZATION);

                if (token != null && token.startsWith("Bearer ")) {
                    requestTemplate.header(HttpHeaders.AUTHORIZATION, token);
                }
            }
        };
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}