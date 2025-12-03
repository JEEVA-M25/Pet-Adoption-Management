//AdoptionRequestRepository.java
package com.example.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.pet.model.AdoptionRequest;

public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest,Long>{

    // Add this method to your AdoptionRequestRepository
    List<AdoptionRequest> findByApplicantEmail(String email);
    List<AdoptionRequest> findByPetIdIn(List<Long> petIds);

    @Modifying
    @Query("UPDATE AdoptionRequest r SET r.status = 'Rejected' WHERE r.pet.id = :petId AND r.id <> :approvedRequestId")
    void rejectOtherRequests(Long petId, Long approvedRequestId);

    
}
