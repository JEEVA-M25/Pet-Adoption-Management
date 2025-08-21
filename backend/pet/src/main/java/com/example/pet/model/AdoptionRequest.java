package com.example.pet.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionRequest {
    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @NotNull(message =  "Pet ID is required")
    private Long petId;

    @NotBlank(message =  "Applicant name is required")
    @Size(min = 2, message =  "Applicant name must be at least 2 characters long")
    private String applicantName;

    @NotBlank(message =  "Applicant email is required")
    @Email(message =  "Invalid email format")
    private String applicantEmail;

    @NotBlank(message =  "Applicant phone is required")
    private String applicantPhone;

   
    private String Status ;
    private LocalDateTime SubmissionDate;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    @JsonBackReference
    private User applicant;

}