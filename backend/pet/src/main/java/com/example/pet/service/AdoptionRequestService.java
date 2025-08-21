package com.example.pet.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pet.model.AdoptionRequest;
import com.example.pet.model.Pet;
import com.example.pet.repository.AdoptionRequestRepository;
import com.example.pet.repository.PetRepository;

@Service
public class AdoptionRequestService {

    private final AdoptionRequestRepository adoptRepo;
    private final PetRepository petRepo;

    public AdoptionRequestService(AdoptionRequestRepository adoptRepo, PetRepository petRepo) {
        this.adoptRepo = adoptRepo;
        this.petRepo = petRepo;
    }

    public List<AdoptionRequest> getAllAdoptionRequests() {
        return adoptRepo.findAll();
    }

    public AdoptionRequest createAdoptionRequest(AdoptionRequest adoption) {
        // Get pet id from relationship
        Long petId = adoption.getPet().getId();

        // Validate pet exists
        Pet pet = petRepo.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found with id: " + petId));

        // Check availability
        if (!"Available".equalsIgnoreCase(pet.getAdoptionStatus())) {
            throw new IllegalArgumentException("Pet is not available for adoption");
        }

        // Set default values
        adoption.setStatus("Pending");
        adoption.setSubmissionDate(LocalDateTime.now());

        return adoptRepo.save(adoption);
    }
    public void deleteAdoptionRequest(Long id) {
    if (!adoptRepo.existsById(id)) {
        throw new IllegalArgumentException("Adoption request not found with id: " + id);
    }
    adoptRepo.deleteById(id);
}

public AdoptionRequest updateAdoptionRequest(Long id, AdoptionRequest updatedRequest) {
    AdoptionRequest existing = adoptRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Adoption request not found with id: " + id));

    // Only allow status update for now
    existing.setStatus(updatedRequest.getStatus());

    return adoptRepo.save(existing);
}

}
