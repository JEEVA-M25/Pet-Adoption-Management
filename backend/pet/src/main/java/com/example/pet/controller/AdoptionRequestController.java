// AdoptionRequestController.java
package com.example.pet.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.pet.model.AdoptionRequest;
import com.example.pet.service.AdoptionRequestService;

@RestController
@RequestMapping("/api/adoption-requests")
public class AdoptionRequestController {

    private final AdoptionRequestService adoptService;

    public AdoptionRequestController(AdoptionRequestService adoptService) {
        this.adoptService = adoptService;
    }

    // ORG_USER and ADMIN only - view ALL requests
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<AdoptionRequest> getAllAdoptionRequests() {
        return adoptService.getAllAdoptionRequests();
    }

    // Req for my shelter - ORG_USER only
    @PreAuthorize("hasRole('ORG_USER')")
    @GetMapping("/shelter")
    public ResponseEntity<List<AdoptionRequest>> getRequestsForMyShelter(Authentication auth) {

        String orgUserEmail = auth.getName();
        List<AdoptionRequest> requests = adoptService.getRequestsForShelter(orgUserEmail);

        return ResponseEntity.ok(requests);
    }


    // NEW: Any authenticated user can view THEIR OWN requests
    @PreAuthorize("hasRole('PUBLIC_USER')")
    @GetMapping("/my-requests")
    public ResponseEntity<List<AdoptionRequest>> getMyAdoptionRequests(Authentication authentication) {
        String userEmail = authentication.getName();
        List<AdoptionRequest> requests = adoptService.getMyAdoptionRequests(userEmail);
        return ResponseEntity.ok(requests);
    }

    // Authenticated users can create requests
    @PreAuthorize("hasRole('PUBLIC_USER')")
    @PostMapping
    public ResponseEntity<AdoptionRequest> createAdoptionRequest(
            @RequestBody AdoptionRequest adoptionRequest,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        AdoptionRequest createdRequest = adoptService.createAdoptionRequest(adoptionRequest, userEmail);
        return ResponseEntity.ok(createdRequest);
    }

    // ORG_USER and ADMIN only - delete ANY request
    @PreAuthorize("hasAnyRole('ORG_USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdoptionRequest(@PathVariable Long id) {
        try {
            adoptService.deleteAdoptionRequest(id);
            return new ResponseEntity<>("Adoption request deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ORG_USER and ADMIN only - update status
    @PreAuthorize("hasAnyRole('ORG_USER','ADMIN')")
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

    // Users can delete their own requests
    @PreAuthorize("hasRole('PUBLIC_USER')")
    @DeleteMapping("/me/{id}")
    public ResponseEntity<?> deleteOwnAdoptionRequest(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        adoptService.deleteOwnAdoptionRequest(id, email);
        return ResponseEntity.ok().build();
    }
}