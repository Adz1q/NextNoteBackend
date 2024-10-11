package org.adz1q.nextnote.repository;

import org.adz1q.nextnote.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findByUserId(int userId);
}