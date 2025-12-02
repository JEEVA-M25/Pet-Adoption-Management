// PetRepository.java
package com.example.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.pet.model.Pet;

public interface PetRepository extends JpaRepository<Pet,Long>{

     List<Pet> findBySpeciesIgnoreCase(String species);
    List<Pet> findByNameContainingIgnoreCase(String name);
    List<Pet> findByAdoptionStatusIgnoreCase(String status);
    @Query("SELECT p FROM Pet p WHERE p.postedBy.shelter.id = :shelterId")
    List<Pet> findPetsByShelterId(Long shelterId);

}
