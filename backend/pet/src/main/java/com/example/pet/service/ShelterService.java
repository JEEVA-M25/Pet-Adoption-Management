package com.example.pet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public ShelterService(ShelterRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }


    public Shelter getShelterById(Long shelterId) {
        return repository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found with id: " + shelterId));
    }

    public Page<Shelter> getAllShelters(Pageable pageable) {
        return repository.findAll(pageable);
    }
@Transactional
public Shelter createShelter(Shelter shelter) {
    // Step 1: Save empty shelter first
    Shelter savedShelter = repository.save(new Shelter(
            null,
            shelter.getName(),
            shelter.getAddress(),
            shelter.getPhone(),
            null,
            new ArrayList<>()
    ));

    // Step 2: Save admin if present
    if (shelter.getAdmin() != null) {
        User admin = shelter.getAdmin();
        admin.setRole(User.Role.ADMIN);
        admin.setShelter(savedShelter);
        admin = userRepository.save(admin);
        savedShelter.setAdmin(admin);
    }

    // Step 3: Save ORG_USERS
    if (shelter.getUsers() != null) {
        List<User> savedUsers = new ArrayList<>();
        for (User u : shelter.getUsers()) {
            u.setRole(User.Role.ORG_USER);
            u.setShelter(savedShelter);
            savedUsers.add(userRepository.save(u));
        }
        savedShelter.setUsers(savedUsers);
    }

    // Step 4: Save shelter with relationships
    return repository.save(savedShelter);
}

@Transactional
public Shelter updateShelter(Long id, Shelter updated) {
    Shelter existing = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Shelter not found"));

    existing.setName(updated.getName());
    existing.setAddress(updated.getAddress());
    existing.setPhone(updated.getPhone());

    // Update or add admin
    if (updated.getAdmin() != null) {
        User admin = updated.getAdmin();
        admin.setRole(User.Role.ADMIN);
        admin.setShelter(existing);

        if (existing.getAdmin() == null) {
            existing.setAdmin(userRepository.save(admin));
        } else {
            User existingAdmin = existing.getAdmin();
            existingAdmin.setName(admin.getName());
            existingAdmin.setUsername(admin.getUsername());
            existingAdmin.setPassword(admin.getPassword());
            existingAdmin.setEmail(admin.getEmail());
            existingAdmin.setPhone(admin.getPhone());
        }
    }

    // Update ORG_USERS
    List<User> updatedUsers = updated.getUsers() != null ? updated.getUsers() : new ArrayList<>();

    // Remove deleted users
    existing.getUsers().removeIf(existingUser ->
            updatedUsers.stream().noneMatch(u -> u.getId() != null && u.getId().equals(existingUser.getId()))
    );

    // Add or update ORG_USERS
    for (User user : updatedUsers) {
        if (user.getId() == null) {
            user.setRole(User.Role.ORG_USER);
            user.setShelter(existing);
            existing.getUsers().add(userRepository.save(user));
        } else {
            existing.getUsers().stream()
                    .filter(u -> u.getId().equals(user.getId()))
                    .findFirst()
                    .ifPresent(u -> {
                        u.setName(user.getName());
                        u.setEmail(user.getEmail());
                        u.setUsername(user.getUsername());
                        u.setPhone(user.getPhone());
                        u.setPassword(user.getPassword());
                        u.setRole(User.Role.ORG_USER);
                    });
        }
    }

    return repository.save(existing);
}

    @Transactional
    public void deleteShelter(Long shelterId) {
        Shelter shelter = repository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found"));

        if (shelter.getAdmin() != null) {
            userRepository.delete(shelter.getAdmin());
        }

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
    public User addOrgUserToShelter(Long shelterId, User orgUser, Long adminId) {
        Shelter shelter = repository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!User.Role.ADMIN.equals(admin.getRole()) || admin.getShelter() == null
                || !shelterId.equals(admin.getShelter().getId())) {
            throw new RuntimeException("Only the shelter's admin can add ORG_USERs");
        }

        orgUser.setShelter(shelter);
        orgUser.setRole(User.Role.ORG_USER);

        User saved = userRepository.save(orgUser);
        shelter.getUsers().add(saved);
        repository.save(shelter);

        return saved;
    }

    @Transactional
    public User addAdminToShelter(Long shelterId, User newAdmin) {
        Shelter shelter = repository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found"));

        if (shelter.getAdmin() != null) {
            throw new RuntimeException("Shelter already has an admin");
        }

        newAdmin.setRole(User.Role.ADMIN);
        newAdmin.setShelter(shelter);
        User savedAdmin = userRepository.save(newAdmin);

        shelter.setAdmin(savedAdmin);
        repository.save(shelter);

        return savedAdmin;
    }
}
