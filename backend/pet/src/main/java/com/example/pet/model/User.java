package com.example.pet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    public enum Role {
        ADMIN,
        ORG_USER,
        PUBLIC_USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String password;
    private String phone;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Shelter for ORG_USER
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @JsonBackReference("shelter-users")
    private Shelter shelter;

    // Pets posted by this user
    @OneToMany(mappedBy = "postedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-pets")
    private List<Pet> pets;

    // Adoption requests by this user
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-requests")
    private List<AdoptionRequest> requests;
}
