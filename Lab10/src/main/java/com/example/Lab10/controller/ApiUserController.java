package com.example.Lab10.controller;

import java.util.Map;
import java.util.HashMap;
import com.example.Lab10.dto.UserRegistrationDto;
import com.example.Lab10.model.User;
import com.example.Lab10.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {

    private final UserService userService;

    public ApiUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto dto) {
        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(dto.getPassword());

        userService.registerUser(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getServerInfo(@RequestHeader(value = "User-Agent", defaultValue = "Unknown") String userAgent) {
        Map<String, String> response = new HashMap<>();
        response.put("userAgent", userAgent);
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/secret")
    @PreAuthorize("hasRole('ADMIN')")
    public String getSecret() {
        return "This is a secret message only for ADMINS.";
    }

    @GetMapping("/user/profile")
    @PreAuthorize("hasRole('USER')")
    public String getUserProfile() {
        return "This is your private user profile.";
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok("This is a protected resource. If you see this, you are authenticated!");
    }
}