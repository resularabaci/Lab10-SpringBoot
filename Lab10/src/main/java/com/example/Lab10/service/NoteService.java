package com.example.Lab10.service;

import com.example.Lab10.model.Note;
import com.example.Lab10.model.User;
import com.example.Lab10.repository.NoteRepository;
import com.example.Lab10.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public Note createNote(String content, String email) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setContent(content);
        note.setUser(owner);
        return noteRepository.save(note);
    }

    public List<Note> getMyNotes(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return noteRepository.findByUserId(user.getId());
    }

    public void deleteNote(Long noteId, String email) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getEmail().equals(email)) {
            throw new RuntimeException("403 Forbidden: You do not own this note");
        }
        noteRepository.delete(note);
    }
}