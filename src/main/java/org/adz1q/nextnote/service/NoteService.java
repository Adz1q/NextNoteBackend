package org.adz1q.nextnote.service;

import org.adz1q.nextnote.model.Note;
import org.adz1q.nextnote.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public ResponseEntity<Object> createNote(Note note) {
        noteRepository.save(note);
        return ResponseEntity.ok(note);
    }

    public ResponseEntity<Object> updateNote(int id, Note note) {
        Optional<Note> optionalNote = noteRepository.findById(id);

        if(!optionalNote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note existingNote = optionalNote.get();

        existingNote.setTitle(note.getTitle());
        existingNote.setContent(note.getContent());

        noteRepository.save(existingNote);
        return ResponseEntity.ok(existingNote);
    }

    public ResponseEntity<Object> getNote(int id) {
        Optional<Note> optionalNote = noteRepository.findById(id);

        if(!optionalNote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note note = optionalNote.get();
        return ResponseEntity.ok(note);
    }

    public List<Note> getNotesByUserId(int userId) {
        return noteRepository.findByUserId(userId);
    }


    public ResponseEntity<Object> deleteNote(int id) {
        Optional<Note> optionalNote = noteRepository.findById(id);

        if(!optionalNote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        noteRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}