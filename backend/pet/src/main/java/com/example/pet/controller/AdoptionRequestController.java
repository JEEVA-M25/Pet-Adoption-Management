package com.example.pet.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        @PutMapping("/{id}")
        public ResponseEntity<?> updateAdoptionRequest(@PathVariable Long id, @RequestBody AdoptionRequest adoption) {
        try {
            AdoptionRequest updated = adoptService.updateAdoptionRequest(id, adoption);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
        }
}
        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteAdoptionRequest(@PathVariable Long id) {
            try {
                adoptService.deleteAdoptionRequest(id);
                return new ResponseEntity<>("Adoption request deleted successfully", HttpStatus.NO_CONTENT);
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
            }
        }

}
