// ShelterService.java
package com.example.pet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pet.model.Pet;
import com.example.pet.model.Shelter;
import com.example.pet.model.User;
import com.example.pet.repository.ShelterRepository;
import com.example.pet.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ShelterService {

    private final ShelterRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ShelterService(ShelterRepository repository, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Shelter getShelterById(Long shelterId) {
        return repository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found with id: " + shelterId));
    }

    public Page<Shelter> getAllShelters(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public Shelter createShelter(Shelter shelter, String adminEmail) {
        User adminUser = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        if (!User.Role.ADMIN.equals(adminUser.getRole())) {
            throw new RuntimeException("Only ADMIN users can create shelters");
        }

        Shelter newShelter = new Shelter();
        newShelter.setName(shelter.getName());
        newShelter.setAddress(shelter.getAddress());
        newShelter.setPhone(shelter.getPhone());
        newShelter.setUsers(new ArrayList<>());

        return repository.save(newShelter);
    }

    @Transactional
    public User addOrgUserToShelter(Long shelterId, User orgUser, String adminEmail) {
        Shelter shelter = repository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found with id: " + shelterId));

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        if (!User.Role.ADMIN.equals(admin.getRole())) {
            throw new RuntimeException("Only global ADMIN can add ORG_USERs");
        }

        if (userRepository.existsByEmail(orgUser.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        orgUser.setShelter(shelter);
        orgUser.setRole(User.Role.ORG_USER);
        orgUser.setPassword(passwordEncoder.encode(orgUser.getPassword()));

        User savedUser = userRepository.save(orgUser);

        shelter.getUsers().add(savedUser);
        repository.save(shelter);

        return savedUser;
    }

    @Transactional
    public void deleteShelter(Long shelterId) {
        Shelter shelter = repository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found"));

        repository.delete(shelter);
    }

    public List<Pet> getPetsInShelter(Long shelterId) {
        Shelter shelter = getShelterById(shelterId);
        List<Pet> pets = new ArrayList<>();
        for (User user : shelter.getUsers()) {
            if (user.getPets() != null) {
                pets.addAll(user.getPets());
            }
        }
        return pets;
    }

    public List<User> getUsersInShelter(Long shelterId) {
        Shelter shelter = getShelterById(shelterId);
        return shelter.getUsers();
    }
    @Transactional
public User convertExistingUserToOrgUser(Long shelterId, Long userId, String adminEmail) {

    // 1. Check admin
    User admin = userRepository.findByEmail(adminEmail)
            .orElseThrow(() -> new RuntimeException("Admin user not found"));

    if (!User.Role.ADMIN.equals(admin.getRole())) {
        throw new RuntimeException("Only ADMIN can convert users");
    }

    // 2. Check shelter exists
    Shelter shelter = repository.findById(shelterId)
            .orElseThrow(() -> new RuntimeException("Shelter not found with id: " + shelterId));

    // 3. Get user
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

    // 4. Only PUBLIC_USER can be upgraded
    if (!User.Role.PUBLIC_USER.equals(user.getRole())) {
        throw new RuntimeException("Only PUBLIC_USER can be converted to ORG_USER");
    }

    // 5. Convert user
    user.setRole(User.Role.ORG_USER);
    user.setShelter(shelter);

    // 6. Update relational mapping
    shelter.getUsers().add(user);

    return userRepository.save(user);
}
@Transactional
public Shelter updateShelter(Long id, Shelter updated) {
    Shelter shelter = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Shelter not found with id: " + id));

    if (updated.getName() != null)
        shelter.setName(updated.getName());

    if (updated.getAddress() != null)
        shelter.setAddress(updated.getAddress());

    if (updated.getPhone() != null)
        shelter.setPhone(updated.getPhone());

    return repository.save(shelter);
}


}
