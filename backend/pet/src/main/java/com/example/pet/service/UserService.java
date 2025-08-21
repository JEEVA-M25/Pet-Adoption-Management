package com.example.pet.service;
import com.example.pet.model.User;
import com.example.pet.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User updatedUser) 
    {
    return userRepository.findById(id).map(user -> {
        user.setUsername(updatedUser.getUsername());
        user.setPassword(updatedUser.getPassword());
        user.setEmail(updatedUser.getEmail());
        user.setRole(updatedUser.getRole());
        user.setShelter(updatedUser.getShelter());
        return userRepository.save(user);
    }).orElse(null);
    }


    public boolean deleteUser(Long id) {
    return userRepository.findById(id).map(user -> {
        userRepository.delete(user);
        return true;
    }).orElse(false);
}

}
