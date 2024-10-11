package org.adz1q.nextnote.controller;

import org.adz1q.nextnote.model.Note;
import org.adz1q.nextnote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/db/note")
public class NoteController {
    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> createNote(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> updateNote(@PathVariable int id, @RequestBody Note note) {
        return noteService.updateNote(id, note);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getNote(@PathVariable int id) {
        return noteService.getNote(id);
    }

    @GetMapping("/user/{userId}")
    public List<Note> getNotesByUserId(@PathVariable int userId) {
        return noteService.getNotesByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNote(@PathVariable int id, @RequestParam String username, @RequestParam String password) {
        return noteService.deleteNote(id, username, password);
    }
}