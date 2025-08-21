package com.example.pet.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pet.model.Pet;
import com.example.pet.model.Shelter;
import com.example.pet.model.User;
import com.example.pet.service.ShelterService;


@RestController
@RequestMapping("/api/shelters")
public class ShelterController {

    private final ShelterService service;
    @Autowired
    public ShelterController(ShelterService service) {
        this.service = service;
    }

    @PostMapping
    public Shelter createShelter(@RequestBody Shelter shelter) {
        return service.createShelter(shelter);
    }

    @GetMapping("/{id}")
    public Shelter getShelter(@PathVariable Long id) {
        return service.getShelterById(id);
    }

    @GetMapping
    public Page<Shelter> getAllShelters(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return service.getAllShelters(PageRequest.of(page, size));
    }

    // @PutMapping("/{id}")
    // public Shelter updateShelter(@PathVariable Long id, @RequestBody Shelter updated) {
    //     return service.updateShelter(id, updated);
    // }

    @DeleteMapping("/{id}")
    public void deleteShelter(@PathVariable Long id) {
        service.deleteShelter(id);
    }
    
    // 🔁 All pets in a particular shelter
    @GetMapping("/{id}/pets")
    public List<Pet> getPetsInShelter(@PathVariable Long id) {
        return service.getPetsInShelter(id);
    }

    // 🔁 All users in a particular shelter
    @GetMapping("/{id}/users")
    public List<User> getUsersInShelter(@PathVariable Long id) {
        return service.getUsersInShelter(id);
    }

    @PostMapping("/{shelterId}/users")
    public User addOrgUser(@PathVariable Long shelterId,
                       @RequestBody User orgUser,
                       @RequestParam Long adminId) {

    // adminId = ID of the admin performing the action
    return service.addOrgUserToShelter(shelterId, orgUser, adminId);
    }

        

}
