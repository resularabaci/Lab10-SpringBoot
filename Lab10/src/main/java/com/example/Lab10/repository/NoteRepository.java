package com.example.Lab10.repository;

import com.example.Lab10.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserId(Long userId);

    @Query(value = "SELECT * FROM notes WHERE user_id = :uId AND content LIKE %:keyword%", nativeQuery = true)
    List<Note> searchMyNotes(@Param("uId") Long userId, @Param("keyword") String keyword);
}