package com.example.Lab10.model;

import jakarta.persistence.*;
import lombok.Data; // Auto-generates getters/setters

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
}