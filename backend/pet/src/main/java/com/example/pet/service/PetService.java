// PetService.java
package com.example.pet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.pet.model.Pet;
import com.example.pet.model.User;
import com.example.pet.repository.PetRepository;
import com.example.pet.repository.UserRepository;

@Service
public class PetService {

    
    private PetRepository petRepo;
    private UserRepository userRepo;

    public PetService(PetRepository petRepo, UserRepository userRepo)
    {
        this.petRepo = petRepo;
        this.userRepo = userRepo;
    }
    public List<Pet> getAllPets()
    {
        return petRepo.findAll();
    }

public List<Pet> getPetsByStatus(String status) {
    return petRepo.findByAdoptionStatusIgnoreCase(status);
}

public List<Pet> getAdoptedPets(String userEmail) {
    return petRepo.findByAdoptedByEmail(userEmail);
}

// PetService.java

public List<Pet> getPetsForOrgUser(String email) {

    User orgUser = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (orgUser.getShelter() == null) {
        throw new RuntimeException("ORG_USER is not assigned to any shelter");
    }

    Long shelterId = orgUser.getShelter().getId();

    return petRepo.findPetsByShelterId(shelterId);
}

// Updated method - automatically set the logged-in user as postedBy
        public Pet createPet(Pet pet, String userEmail) {
            User user = userRepo.findByEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
            pet.setPostedBy(user);  // Set the authenticated user
            user.getPets().add(pet); // Update user's pets list
            
            return petRepo.save(pet);
        }


    public Optional<Pet> getPetById(Long id)
    {
         return petRepo.findById(id);
    }
    public List<Pet> getPetsBySpecies(String species) {
    return petRepo.findBySpeciesIgnoreCase(species);
}

    public List<Pet> searchPetsByName(String name) {
        return petRepo.findByNameContainingIgnoreCase(name);
    }

      public Optional<Pet> updatePet(Long id, Pet petDetails) {
        return petRepo.findById(id).map(existingPet -> {
            existingPet.setName(petDetails.getName());
            existingPet.setSpecies(petDetails.getSpecies());
            existingPet.setBreed(petDetails.getBreed());
            existingPet.setAge(petDetails.getAge());
            existingPet.setDescription(petDetails.getDescription());
            existingPet.setImageUrl(petDetails.getImageUrl());
            existingPet.setAdoptionStatus(petDetails.getAdoptionStatus());
            return petRepo.save(existingPet);
        });
    }

    public boolean deletePet(Long id) {
        return petRepo.findById(id).map(pet -> {
            petRepo.delete(pet);
            return true;
        }).orElse(false);
    }
}
