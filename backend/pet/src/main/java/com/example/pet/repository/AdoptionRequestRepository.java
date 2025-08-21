package com.example.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pet.model.AdoptionRequest;

public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest,Long>{

}
