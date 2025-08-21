package com.example.pet.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.pet.model.Pet;
import com.example.pet.model.Shelter;
import com.example.pet.model.User;
import com.example.pet.repository.ShelterRepository;

import jakarta.transaction.Transactional;

@Service
public class ShelterService {

    private final ShelterRepository repository;

    public ShelterService(ShelterRepository repository) {
        this.repository = repository;
    }

    public Shelter createShelter(Shelter shelter) {
        return repository.save(shelter);
    }

    public Shelter getShelterById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Shelter not found"));
    }

    public Page<Shelter> getAllShelters(Pageable pageable) {
        return repository.findAll(pageable);
    }

  @Transactional
public Shelter updateShelter(Long id, Shelter updated) {
    Shelter existing = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Shelter not found"));

    existing.setName(updated.getName());
    existing.setAddress(updated.getAddress());
    existing.setPhone(updated.getPhone());

    // Handle users safely
    existing.getUsers().clear();
    if (updated.getUsers() != null) {
        for (User user : updated.getUsers()) {
            user.setShelter(existing); // maintain bi-directional link
            existing.getUsers().add(user);
        }
    }

    return repository.save(existing);
}




    public void deleteShelter(Long id) {
    if (!repository.existsById(id)) {
        throw new RuntimeException("Shelter not found");
    }
    repository.deleteById(id);
}


    public List<Pet> getPetsInShelter(Long shelterId) {
        Shelter shelter = getShelterById(shelterId);
        return shelter.getUsers().stream()
                .flatMap(user -> user.getPets().stream())
                .collect(Collectors.toList());
    }

    public List<User> getUsersInShelter(Long shelterId) {
        Shelter shelter = getShelterById(shelterId);
        return shelter.getUsers();
    }
}
