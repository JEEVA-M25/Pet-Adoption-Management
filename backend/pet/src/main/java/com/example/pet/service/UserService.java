// UserService.java
package com.example.pet.service;
import com.example.pet.model.User;
import com.example.pet.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // <--- 

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;   // <--- 
    }
    
   // In your createUser method, set default role to PUBLIC_USER
public User createUser(User user) {
    if (userRepository.existsByUsername(user.getUsername())) {
        throw new IllegalArgumentException("Username already exists");
    }
    if (userRepository.existsByEmail(user.getEmail())) {
        throw new IllegalArgumentException("Email already exists");
    }

    // Set default role to PUBLIC_USER if not specified
    if (user.getRole() == null) {
        user.setRole(User.Role.PUBLIC_USER);
    }

    // ✅ Check if role is valid (only among ADMIN, ORG_USER, PUBLIC_USER)
    if (user.getRole() != User.Role.ADMIN &&
        user.getRole() != User.Role.ORG_USER &&
        user.getRole() != User.Role.PUBLIC_USER) {
        throw new IllegalArgumentException("Improper role specified");
    }

    // Hash password before saving
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
}

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

public User updateUser(Long id, User updatedUser) {
    return userRepository.findById(id).map(user -> {

        // Username uniqueness check
        if (updatedUser.getUsername() != null &&
            !user.getUsername().equals(updatedUser.getUsername()) &&
            userRepository.existsByUsername(updatedUser.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Email uniqueness check
        if (updatedUser.getEmail() != null &&
            !user.getEmail().equals(updatedUser.getEmail()) &&
            userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Update fields safely (only if provided)
        if (updatedUser.getName() != null)
            user.setName(updatedUser.getName());

        if (updatedUser.getPhone() != null)
            user.setPhone(updatedUser.getPhone());

        if (updatedUser.getUsername() != null)
            user.setUsername(updatedUser.getUsername());

        if (updatedUser.getEmail() != null)
            user.setEmail(updatedUser.getEmail());

        if (updatedUser.getRole() != null)
            user.setRole(updatedUser.getRole());

        if (updatedUser.getShelter() != null)
            user.setShelter(updatedUser.getShelter());

        // Handle password only if provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(user);
    }).orElse(null);
}

// OR Method 2: Using findById with proper error handling
    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return true;
        }
        return false;
    }
}
