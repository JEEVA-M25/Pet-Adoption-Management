package com.example.pet.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pet.model.AdoptionRequest;
import com.example.pet.service.AdoptionRequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/adoption-requests")

public class AdoptionRequestController {

    AdoptionRequestService adoptService;

    public AdoptionRequestController(AdoptionRequestService adoptService)
    {
        this.adoptService = adoptService;
    }
    @GetMapping
    public List<AdoptionRequest> getAllAdoptionRequests()
    {
        return adoptService.getAllAdoptionRequests();
    }

    @PostMapping
    public ResponseEntity<?> createAdoptionRequest(@Valid @RequestBody AdoptionRequest adoption)
    {
        try{
            AdoptionRequest created = adoptService.createAdoptionRequest(adoption);
            return new ResponseEntity<>(created,HttpStatus.CREATED);
        }
        catch(IllegalArgumentException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",ex.getMessage()));
        }
    }
   
        @DeleteMapping("/{id}")
public ResponseEntity<String> deleteAdoptionRequest(@PathVariable Long id) {
    try {
        adoptService.deleteAdoptionRequest(id);
        return new ResponseEntity<>("Adoption request deleted successfully", HttpStatus.OK);
    } catch (IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}


     @PatchMapping("/{id}")
public ResponseEntity<?> updateAdoptionRequestStatus(
        @PathVariable Long id,
        @RequestBody Map<String, String> updates) {

    String newStatus = updates.get("status");
    if (newStatus == null) {
        return ResponseEntity.badRequest()
                .body(Map.of("message", "Status field is required"));
    }

    try {
        AdoptionRequest temp = new AdoptionRequest();
        temp.setStatus(newStatus);
        AdoptionRequest updatedRequest = adoptService.updateAdoptionRequest(id, temp);
        return ResponseEntity.ok(updatedRequest);
    } catch (IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("message", ex.getMessage()));
    }
}



}
