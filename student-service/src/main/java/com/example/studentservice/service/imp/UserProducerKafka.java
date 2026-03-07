package com.example.studentservice.service.imp;


import com.example.studentservice.dto.AuthDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducerKafka {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserProducerKafka(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserRegisteredEvent(AuthDto event) {

        kafkaTemplate.send("user-registered", event);

    }
}
