package com.example.Lab10.security;

import com.example.Lab10.model.RefreshToken;
import com.example.Lab10.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveToken(String token, String email) {
        repository.deleteByUserEmail(email);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUserEmail(email);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(604800));
        repository.save(refreshToken);
    }

    public boolean isValid(String token) {
        return repository.findByToken(token).isPresent();
    }

    @Transactional
    public boolean invalidateToken(String token) {
        if (token == null) return false;

        Optional<RefreshToken> tokenOpt = repository.findByToken(token);

        if (tokenOpt.isPresent()) {
            repository.deleteByToken(token);
            return true;
        }

        return false;
    }

    @Transactional
    public void rotateToken(String oldToken, String newToken, String email) {
        repository.deleteByToken(oldToken);
        saveToken(newToken, email);
    }
}