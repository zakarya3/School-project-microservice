package com.example.enrollmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String nomCours;
    private String codeCours;
    private String description;
    private int volumeHoraire;
    private String semestre;
    private String anneeScolaire;
    private String statut;
}

