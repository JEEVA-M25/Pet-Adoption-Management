package com.example.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pet.model.AdoptionRequest;

public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest,Long>{

    // Add this method to your AdoptionRequestRepository
    List<AdoptionRequest> findByApplicantEmail(String email);
}
