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

            // Check uniqueness on update
            if (!user.getUsername().equals(updatedUser.getUsername()) &&
                userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }

            if (!user.getEmail().equals(updatedUser.getEmail()) &&
                userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }

            user.setUsername(updatedUser.getUsername());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());
            user.setShelter(updatedUser.getShelter());
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
