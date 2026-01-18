package com.example.Lab10.controller;

import com.example.Lab10.model.Note;
import com.example.Lab10.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, String> body, Authentication auth) {
        Note note = noteService.createNote(body.get("content"), auth.getName());
        return ResponseEntity.status(201).body(note);
    }

    @GetMapping
    public ResponseEntity<List<Note>> getAll(Authentication auth) {
        return ResponseEntity.ok(noteService.getMyNotes(auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        try {
            noteService.deleteNote(id, auth.getName());
            return ResponseEntity.ok(Map.of("message", "Note deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }
}