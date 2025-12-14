package com.example.Lab10.service;

import com.example.Lab10.model.User;
import com.example.Lab10.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    // Simple check (in real life, passwords are encrypted!)
    public boolean authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .map(u -> u.getPassword().equals(rawPassword))
                .orElse(false);
    }
}