package org.adz1q.nextnote.service;

import org.adz1q.nextnote.model.Note;
import org.adz1q.nextnote.model.User;
import org.adz1q.nextnote.repository.NoteRepository;
import org.adz1q.nextnote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final MessageDigest messageDigest;
    private final UserRepository userRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepository) throws NoSuchAlgorithmException {
        this.noteRepository = noteRepository;
        this.messageDigest = MessageDigest.getInstance("SHA-256");
        this.userRepository = userRepository;
    }

    private String hashPassword(String password) {
        byte[] hashedBytes = messageDigest.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public ResponseEntity<Object> createNote(Note note) {
        noteRepository.save(note);
        return ResponseEntity.ok(note);
    }

    public ResponseEntity<Object> updateNote(int id, Note note) {
        Optional<Note> optionalNote = noteRepository.findById(id);

        if(optionalNote.isEmpty()) {
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

        if(optionalNote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note note = optionalNote.get();
        return ResponseEntity.ok(note);
    }

    public List<Note> getNotesByUserId(int userId) {
        return noteRepository.findByUserId(userId);
    }

    public ResponseEntity<Object> deleteNote(int id, String username, String password) {
        Optional<Note> optionalNote = noteRepository.findById(id);

        if(optionalNote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note note = optionalNote.get();
        int userId = note.getUserId();

        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        String realUsername = user.getUsername();
        String realPassword = user.getPassword();

        if(!realUsername.equals(username) || !realPassword.equals(hashPassword(password))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        noteRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}