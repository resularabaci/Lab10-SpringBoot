package com.example.Lab10.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NoteRequest {
    @NotBlank(message = "Content is required")
    @Size(max = 2000, message = "Note too long")
    private String content;
}
