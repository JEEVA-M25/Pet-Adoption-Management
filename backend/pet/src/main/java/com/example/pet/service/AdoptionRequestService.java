// AdoptionRequestService.java
package com.example.pet.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<AdoptionRequest> getRequestsForShelter(String orgUserEmail) {

    // 1. Get ORG_USER
    User orgUser = userRepo.findByEmail(orgUserEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (orgUser.getShelter() == null) {
        throw new RuntimeException("ORG_USER is not assigned to any shelter");
    }

    Long shelterId = orgUser.getShelter().getId();

    // 2. Fetch pets that belong to this shelter
    List<Pet> petsFromShelter = petRepo.findPetsByShelterId(shelterId);

    // 3. Extract pet IDs
    List<Long> petIds = petsFromShelter.stream()
            .map(Pet::getId)
            .toList();

    // 4. Get adoption requests for these pet IDs
    return adoptRepo.findByPetIdIn(petIds);
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


@Transactional
public AdoptionRequest updateAdoptionRequest(Long id, AdoptionRequest updatedRequest) {

    AdoptionRequest existing = adoptRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Adoption request not found with id: " + id));

    String newStatus = updatedRequest.getStatus();

    if (!List.of("Pending", "Approved", "Rejected").contains(newStatus)) {
        throw new IllegalArgumentException("Invalid status value: " + newStatus);
    }

    // Prevent going back from approved
    if (existing.getStatus().equals("Approved") && newStatus.equals("Pending")) {
        throw new IllegalArgumentException("Cannot revert from Approved to Pending");
    }

    existing.setStatus(newStatus);

    // ★★★ AUTOMATIC REJECTION OF OTHER REQUESTS ★★★
    if ("Approved".equals(newStatus)) {

        Pet pet = existing.getPet();

        // 1. Mark pet as adopted
        pet.setAdoptionStatus("Adopted");
        petRepo.save(pet);

        // 2. Reject all other pending requests for this pet
        adoptRepo.rejectOtherRequests(pet.getId(), existing.getId());
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
