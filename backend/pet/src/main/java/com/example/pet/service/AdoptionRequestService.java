package com.example.pet.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
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

 public AdoptionRequest createAdoptionRequest(AdoptionRequest adoptionRequest, String userEmail) {
        // Get the authenticated user
        User applicant = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set applicant information from the user entity
        adoptionRequest.setApplicant(applicant);
        adoptionRequest.setApplicantName(applicant.getName());
        adoptionRequest.setApplicantEmail(applicant.getEmail());
        adoptionRequest.setApplicantPhone(applicant.getPhone());
        
        adoptionRequest.setStatus("Pending");
        adoptionRequest.setSubmissionDate(LocalDateTime.now());

        return adoptRepo.save(adoptionRequest);
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
    public void deleteOwnAdoptionRequest(Long requestId, String authEmail) {
        AdoptionRequest request = adoptRepo.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Adoption request not found"));

        if (!request.getApplicant().getEmail().equals(authEmail)) {
            throw new SecurityException("You can only delete your own adoption requests");
        }

        adoptRepo.delete(request);
    }
    // Add this method to your AdoptionRequestService class
public List<AdoptionRequest> getMyAdoptionRequests(String userEmail) {
    return adoptRepo.findByApplicantEmail(userEmail);
}

}
