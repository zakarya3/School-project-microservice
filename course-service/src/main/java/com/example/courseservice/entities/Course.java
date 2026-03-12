package com.example.courseservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor

@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomCours;

    @Column(unique = true)
    private String codeCours;

    private String description;

    private int volumeHoraire;

    private String semestre;

    private String anneeScolaire;

    private String statut;
}

