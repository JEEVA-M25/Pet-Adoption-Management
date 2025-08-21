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
    private final PasswordEncoder passwordEncoder; // ← add this

    public ShelterService(ShelterRepository repository, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) { // ← inject here
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // ← assign
    }


    public Shelter getShelterById(Long shelterId) {
        return repository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found with id: " + shelterId));
    }

@Transactional
public Shelter createShelter(Shelter shelter, String adminEmail) {
    // Get the admin user who is creating the shelter
    User adminUser = userRepository.findByEmail(adminEmail)
            .orElseThrow(() -> new RuntimeException("Admin user not found"));
    
    // Verify the user is actually an ADMIN
    if (!User.Role.ADMIN.equals(adminUser.getRole())) {
        throw new RuntimeException("Only ADMIN users can create shelters");
    }

    // Step 1: Create the shelter with the admin
    Shelter newShelter = new Shelter();
    newShelter.setName(shelter.getName());
    newShelter.setAddress(shelter.getAddress());
    newShelter.setPhone(shelter.getPhone());
    newShelter.setAdmin(adminUser);  // Set the creating admin as shelter admin
    newShelter.setUsers(new ArrayList<>());

    // Save the shelter
    Shelter savedShelter = repository.save(newShelter);

    // Update the admin user to reference the shelter
    adminUser.setShelter(savedShelter);
    userRepository.save(adminUser);

    return savedShelter;
}

    public Page<Shelter> getAllShelters(Pageable pageable) {
        return repository.findAll(pageable);
    }
@Transactional
public User addOrgUserToShelter(Long shelterId, User orgUser, String adminEmail) {
    // Get the shelter
    Shelter shelter = repository.findById(shelterId)
            .orElseThrow(() -> new RuntimeException("Shelter not found with id: " + shelterId));

    // Get the admin user making the request
    User admin = userRepository.findByEmail(adminEmail)
            .orElseThrow(() -> new RuntimeException("Admin user not found"));

    // SECURITY CHECK: Verify the admin is actually the admin of this shelter
    if (shelter.getAdmin() == null || !shelter.getAdmin().getId().equals(admin.getId())) {
        throw new RuntimeException("Only the shelter's admin can add ORG_USERs");
    }

    // Check if user already exists with this email
    if (userRepository.existsByEmail(orgUser.getEmail())) {
        throw new RuntimeException("User with this email already exists");
    }

    // Set up the new ORG_USER
    orgUser.setShelter(shelter);
    orgUser.setRole(User.Role.ORG_USER);
    orgUser.setPassword(passwordEncoder.encode(orgUser.getPassword()));

    User savedUser = userRepository.save(orgUser);
    
    // Add to shelter's users list
    shelter.getUsers().add(savedUser);
    repository.save(shelter);

    return savedUser;
}

// @Transactional
// public Shelter updateShelter(Long id, Shelter updated) {
//     Shelter existing = repository.findById(id)
//             .orElseThrow(() -> new RuntimeException("Shelter not found"));

//     existing.setName(updated.getName());
//     existing.setAddress(updated.getAddress());
//     existing.setPhone(updated.getPhone());

//     // Update or add admin
//     if (updated.getAdmin() != null) {
//         User admin = updated.getAdmin();
//         admin.setRole(User.Role.ADMIN);
//         admin.setShelter(existing);

//         if (existing.getAdmin() == null) {
//             existing.setAdmin(userRepository.save(admin));
//         } else {
//             User existingAdmin = existing.getAdmin();
//             existingAdmin.setName(admin.getName());
//             existingAdmin.setUsername(admin.getUsername());
//             existingAdmin.setPassword(admin.getPassword());
//             existingAdmin.setEmail(admin.getEmail());
//             existingAdmin.setPhone(admin.getPhone());
//         }
//     }

//     // Update ORG_USERS
//     List<User> updatedUsers = updated.getUsers() != null ? updated.getUsers() : new ArrayList<>();

//     // Remove deleted users
//     existing.getUsers().removeIf(existingUser ->
//             updatedUsers.stream().noneMatch(u -> u.getId() != null && u.getId().equals(existingUser.getId()))
//     );

//     // Add or update ORG_USERS
//     for (User user : updatedUsers) {
//         if (user.getId() == null) {
//             user.setRole(User.Role.ORG_USER);
//             user.setShelter(existing);
//             existing.getUsers().add(userRepository.save(user));
//         } else {
//             existing.getUsers().stream()
//                     .filter(u -> u.getId().equals(user.getId()))
//                     .findFirst()
//                     .ifPresent(u -> {
//                         u.setName(user.getName());
//                         u.setEmail(user.getEmail());
//                         u.setUsername(user.getUsername());
//                         u.setPhone(user.getPhone());
//                         u.setPassword(user.getPassword());
//                         u.setRole(User.Role.ORG_USER);
//                     });
//         }
//     }

//     return repository.save(existing);
// }

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
