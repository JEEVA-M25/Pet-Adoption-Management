// PetController.java
package com.example.pet.controller;

import com.example.pet.model.Pet;
import com.example.pet.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    
    private PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }
    
    // ORG_USER only
    @PostMapping
    @PreAuthorize("hasRole('ORG_USER')")
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet, Authentication authentication) {
        String userEmail = authentication.getName();
        Pet createdPet = petService.createPet(pet, userEmail);
        return ResponseEntity.status(201).body(createdPet);
    }
    // Public access
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    // Public access
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPetById(@PathVariable Long id) {
        return petService.getPetById(id)
        .<ResponseEntity<Object>>map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(404)
                .body(java.util.Map.of("message","Pet with ID "+id+" not found")));
    }

    // Public access
    @GetMapping("/species/{species}")
    public ResponseEntity<List<Pet>> getPetsBySpecies(@PathVariable String species) {
        return ResponseEntity.ok(petService.getPetsBySpecies(species));
    }

    // Public access
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<Pet>> searchPetsByName(@PathVariable String name) {
        return ResponseEntity.ok(petService.searchPetsByName(name));
    }

    // my adopted pets
    @PreAuthorize("hasRole('PUBLIC_USER')")
@GetMapping("/my-adopted")
public ResponseEntity<List<Pet>> getMyAdoptedPets(Authentication auth) {
    String email = auth.getName();
    return ResponseEntity.ok(petService.getAdoptedPets(email));
}


// PetController.java

@PreAuthorize("hasRole('ORG_USER')")
@GetMapping("/my-pets")
public ResponseEntity<?> getMyPets(Authentication authentication) {

    try {
        String email = authentication.getName();
        List<Pet> pets = petService.getPetsForOrgUser(email);
        return ResponseEntity.ok(pets);

    } catch (RuntimeException e) {
        return ResponseEntity
                .status(400)
                .body(Map.of("message", e.getMessage()));
    }
}


    // ORG_USER only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORG_USER')")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet petDetails) {
        return petService.updatePet(id, petDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ORG_USER only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORG_USER')")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        return petService.deletePet(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
public ResponseEntity<List<Pet>> getPetsByStatus(@PathVariable String status) {
    return ResponseEntity.ok(petService.getPetsByStatus(status));
}



}