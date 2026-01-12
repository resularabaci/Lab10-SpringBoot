package com.example.Lab10.controller;

import com.example.Lab10.security.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.get("email"), request.get("password")));
        String token = jwtUtil.generateToken(request.get("email"));
        return Map.of("token", token);
    }
}