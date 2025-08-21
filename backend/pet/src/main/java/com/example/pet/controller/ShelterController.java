package com.example.pet.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pet.model.Pet;
import com.example.pet.model.Shelter;
import com.example.pet.model.User;
import com.example.pet.service.ShelterService;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/shelters")
public class ShelterController {

    private final ShelterService service;

    public ShelterController(ShelterService service) {
        this.service = service;
    }

    // Admin only - automatically sets the creating admin as shelter admin
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Shelter createShelter(@RequestBody Shelter shelter, Authentication authentication) {
        String adminEmail = authentication.getName(); // Get the logged-in admin's email
        return service.createShelter(shelter, adminEmail);
    }
    // Public access
    @GetMapping("/{id}")
    public Shelter getShelter(@PathVariable Long id) {
        return service.getShelterById(id);
    }

    // Public access
    @GetMapping
    public Page<Shelter> getAllShelters(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return service.getAllShelters(PageRequest.of(page, size));
    }

    // Admin only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteShelter(@PathVariable Long id) {
        service.deleteShelter(id);
    }
    
    // Public access
    @GetMapping("/{id}/pets")
    public List<Pet> getPetsInShelter(@PathVariable Long id) {
        return service.getPetsInShelter(id);
    }

    // Public access
    @GetMapping("/{id}/users")
    public List<User> getUsersInShelter(@PathVariable Long id) {
        return service.getUsersInShelter(id);
    }

   // Only shelter admin can add ORG_USERs
    @PostMapping("/{shelterId}/users")
    public User addOrgUser(@PathVariable Long shelterId,
                          @RequestBody User orgUser,
                          Authentication authentication) {
        
        String adminEmail = authentication.getName(); // Get logged-in admin's email
        return service.addOrgUserToShelter(shelterId, orgUser, adminEmail);
    }
}