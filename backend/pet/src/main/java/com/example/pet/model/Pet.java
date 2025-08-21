package com.example.pet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    
    @Enumerated(EnumType.STRING)
    private AgeCategory ageCategory;
    
    public enum AgeCategory {
        PUPPY_KITTEN, JUVENILE, ADULT
    }
  // Owner (user who posted)
    @ManyToOne
    @JoinColumn(name = "posted_by_id")
    @JsonBackReference("user-pets")
    private User postedBy;

    // Adoption requests for this pet
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("pet-requests")
    private List<AdoptionRequest> adoptionRequests;
}
