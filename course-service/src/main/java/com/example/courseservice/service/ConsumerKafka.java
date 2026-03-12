package com.example.courseservice.service;

import com.example.courseservice.dto.AuthDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerKafka {
    @KafkaListener(topics = "user-registered", groupId = "notification-group")
    public void consume(AuthDto event) {

        System.out.println("Sending notification to: " + event.getEmail());

        // send email / push notification

    }
}