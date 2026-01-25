package com.example.Lab10.service;

import com.example.Lab10.model.RefreshToken;
import com.example.Lab10.repository.RefreshTokenRepository;
import com.example.Lab10.security.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository repository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void testInvalidateToken_Success() {
        String tokenString = "valid-token";
        RefreshToken mockToken = new RefreshToken();
        mockToken.setToken(tokenString);

        when(repository.findByToken(tokenString)).thenReturn(Optional.of(mockToken));

        boolean result = refreshTokenService.invalidateToken(tokenString);

        assertTrue(result, "Should return true when token is found and deleted");
        verify(repository, times(1)).deleteByToken(tokenString);
    }

    @Test
    void testInvalidateToken_Fail_NotFound() {
        String tokenString = "fake-token";
        when(repository.findByToken(tokenString)).thenReturn(Optional.empty());

        boolean result = refreshTokenService.invalidateToken(tokenString);

        assertFalse(result, "Should return false if token doesn't exist");
        verify(repository, never()).deleteByToken(anyString());
    }
}