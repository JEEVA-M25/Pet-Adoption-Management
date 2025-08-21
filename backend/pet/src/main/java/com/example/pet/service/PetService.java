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
    public Pet createPet(Pet pet) {
    User user = pet.getPostedBy(); // assume only ID is set
    if (user != null) {
        User persistedUser = userRepo.findById(user.getId())
                              .orElseThrow(() -> new IllegalArgumentException("User not found"));
        pet.setPostedBy(persistedUser);       // set managed entity
        persistedUser.getPets().add(pet);     // update user's pets list manually
    }
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
