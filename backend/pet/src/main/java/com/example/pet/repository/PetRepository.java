package com.example.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pet.model.Pet;

public interface PetRepository extends JpaRepository<Pet,Long>{

     List<Pet> findBySpeciesIgnoreCase(String species);
    List<Pet> findByNameContainingIgnoreCase(String name);
}
