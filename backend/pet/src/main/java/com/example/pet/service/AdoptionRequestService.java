package com.example.pet.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pet.model.AdoptionRequest;
import com.example.pet.model.Pet;
import com.example.pet.model.User;
import com.example.pet.repository.AdoptionRequestRepository;
import com.example.pet.repository.PetRepository;
import com.example.pet.repository.UserRepository;

@Service
public class AdoptionRequestService {

    private final AdoptionRequestRepository adoptRepo;
    private final PetRepository petRepo;
    private final UserRepository userRepo;

    public AdoptionRequestService(AdoptionRequestRepository adoptRepo, PetRepository petRepo, UserRepository userRepo) {
        this.adoptRepo = adoptRepo;
        this.petRepo = petRepo;
        this.userRepo = userRepo;
    }

    public List<AdoptionRequest> getAllAdoptionRequests() {
        return adoptRepo.findAll();
    }

   public AdoptionRequest createAdoptionRequest(AdoptionRequest adoption) {
    // Validate pet exists
    Long petId = adoption.getPet().getId();
    Pet pet = petRepo.findById(petId)
            .orElseThrow(() -> new IllegalArgumentException("Pet not found with id: " + petId));

    // Validate user exists
    Long userId = adoption.getApplicant().getId();
    User applicant = userRepo.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

    // Check pet availability
    if (!"Available".equalsIgnoreCase(pet.getAdoptionStatus())) {
        throw new IllegalArgumentException("Pet is not available for adoption");
    }

    // Set default values
    adoption.setStatus("Pending");
    adoption.setSubmissionDate(LocalDateTime.now());
    adoption.setApplicant(applicant);  // ✅ set the user
    adoption.setPet(pet);              // ✅ ensure pet reference is set

    return adoptRepo.save(adoption);
}

    public void deleteAdoptionRequest(Long id) {
        AdoptionRequest request = adoptRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adoption request not found"));

        // Manually remove from Pet's list
        if (request.getPet() != null) {
            request.getPet().getAdoptionRequests().remove(request);
        }

        // Delete adoption request (User cascade will handle removal)
        adoptRepo.delete(request);
    }


public AdoptionRequest updateAdoptionRequest(Long id, AdoptionRequest updatedRequest) {
    AdoptionRequest existing = adoptRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Adoption request not found with id: " + id));

    String newStatus = updatedRequest.getStatus();
    if (!List.of("Pending", "Approved", "Rejected").contains(newStatus)) {
        throw new IllegalArgumentException("Invalid status value: " + newStatus);
    }

    // Optional: prevent invalid transitions
    if (existing.getStatus().equals("Approved") && newStatus.equals("Pending")) {
        throw new IllegalArgumentException("Cannot revert from Approved to Pending");
    }

    existing.setStatus(newStatus);

    // Optional: mark pet as adopted if request approved
    if ("Approved".equals(newStatus)) {
        existing.getPet().setAdoptionStatus("Adopted");
        petRepo.save(existing.getPet());
    }

    return adoptRepo.save(existing);
}


}
