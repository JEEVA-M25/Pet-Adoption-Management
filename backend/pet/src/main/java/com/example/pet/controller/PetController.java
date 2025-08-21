package com.example.pet.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


import com.example.pet.model.Pet;
import com.example.pet.service.PetService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/pets")
public class PetController {
    
    private PetService petService;

    public PetController (PetService petService)
    {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<Pet> createPet ( @Valid @RequestBody Pet pet)
    {
        return ResponseEntity.status(201).body(petService.createPet(pet));
    }
    
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets()
    {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPetById(@PathVariable Long id)
    {
        return petService.getPetById(id)
        .<ResponseEntity<Object>>map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(404)
                .body(Map.of("message","Pet with ID "+id+" not found")));
    }
        @GetMapping("/species/{species}")
    public ResponseEntity<List<Pet>> getPetsBySpecies(@PathVariable String species) {
        return ResponseEntity.ok(petService.getPetsBySpecies(species));
    }

    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<Pet>> searchPetsByName(@PathVariable String name) {
        return ResponseEntity.ok(petService.searchPetsByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @Valid @RequestBody Pet petDetails) {
        return petService.updatePet(id, petDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        return petService.deletePet(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

}