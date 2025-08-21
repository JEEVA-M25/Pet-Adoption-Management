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

    AdoptionRequestRepository adoptRepo;
    PetRepository petRepo;

    public AdoptionRequestService (AdoptionRequestRepository adoptRepo,PetRepository petRepo)
    {
        this.adoptRepo = adoptRepo;
        this.petRepo = petRepo;
    }
     public List<AdoptionRequest>getAllAdoptionRequests()
    {
        return adoptRepo.findAll();
    }
    public AdoptionRequest createAdoptionRequest(AdoptionRequest adoption)
    {
        Pet pet = petRepo.findById(adoption.getPetId()).orElseThrow(
                            ()-> new IllegalArgumentException("Pet is not available for adoption"));

        if(!"Available".equalsIgnoreCase(pet.getAdoptionStatus()))
        {
            throw new IllegalArgumentException("Pet is not available for adoption");
        }
        adoption.setStatus("Pending");
        adoption.setSubmissionDate(LocalDateTime.now());
        
        return adoptRepo.save(adoption);
    }
}
