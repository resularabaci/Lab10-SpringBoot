package com.example.Lab10.controller;

import com.example.Lab10.security.JwtUtil;
import com.example.Lab10.security.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.get("password")));

            String access = jwtUtil.generateToken(email);
            String refresh = jwtUtil.generateRefreshToken(email);

            refreshTokenService.saveToken(refresh, email);

            logger.info("Login successful for user: {}", email);
            return ResponseEntity.ok(Map.of("accessToken", access, "refreshToken", refresh));
        } catch (Exception e) {
            logger.warn("Login failed for email: {}", email);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String oldToken = request.get("refreshToken");

        if (oldToken != null && refreshTokenService.isValid(oldToken) && jwtUtil.validateToken(oldToken)) {
            String email = jwtUtil.extractEmail(oldToken);
            String newAccess = jwtUtil.generateToken(email);
            String newRefresh = jwtUtil.generateRefreshToken(email);

            refreshTokenService.rotateToken(oldToken, newRefresh, email);

            logger.info("Token rotated for user: {}", email);
            return ResponseEntity.ok(Map.of("accessToken", newAccess, "refreshToken", newRefresh));
        }

        logger.error("Invalid or reused refresh token attempt."); // 3.1
        return ResponseEntity.status(403).body(Map.of("error", "Invalid refresh token"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        boolean wasDeleted = refreshTokenService.invalidateToken(refreshToken);

        if (wasDeleted) {
            logger.info("User logged out. Session destroyed."); // 3.1
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        } else {
            logger.warn("Logout failed: Session already dead."); // 3.1
            return ResponseEntity.status(401).body(Map.of("error", "Session already ended."));
        }
    }
}