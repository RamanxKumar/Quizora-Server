package com.projects.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String question;

    @NotBlank
    private String subject;

    @NotBlank
    private String questionType;

    @NotEmpty(message = "Choices list must not be empty")
    @ElementCollection
    private List<@NotBlank(message = "Each choice must not be blank") String> choices;

    @NotEmpty(message = "Correct answers list must not be empty")
    @ElementCollection
    private List<@NotBlank(message = "Each correct answer must not be blank") String> correctAnswers;
}
