package com.example.pet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message =  "Name must not be blank")
    private String name;

    @NotBlank(message =  "Species must not be blank")
    private String species;

    @NotBlank(message =  "Breed must not be blank")
    private String breed;

    @Min(value = 0, message =  "Age must not be non-negative")
    private int age;

    private String description;
    private String imageUrl;
    
    @NotBlank(message =  "Adpotion status must not be blank")
    @Pattern(regexp = "Available|Pending|Adopted", message = "Adoption status must be Available, Pending, or Adopted")
    private String adoptionStatus;

      @ManyToOne
    @JoinColumn(name = "posted_by_id")
    @JsonBackReference
    private User postedBy;
    
    @Enumerated(EnumType.STRING)
    private AgeCategory ageCategory;
    
     public enum AgeCategory {
        PUPPY_KITTEN, JUVENILE, ADULT
    }

}