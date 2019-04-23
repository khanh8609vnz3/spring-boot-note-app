package com.example.easynote.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.easynote.exception.ResourceNotFoundException;
import com.example.easynote.model.Note;
import com.example.easynote.repository.NoteRepository;

@RestController
@RequestMapping("/api")
public class NoteController {

	@Autowired
	NoteRepository noteRepository;

	// Test data
	@GetMapping("/notes")
	public ResponseEntity<List<Note>> getAllTestData(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "id", required = false) Long id) {
		List<Note> notes = new ArrayList<>();
		if (name != null || id != null) {
			notes = noteRepository.findAllNotesWithParams(name, id);
//			notes = noteRepository.findAllByNameAndId(name, id);
		} else {
			notes = noteRepository.findAll();
		}
		return new ResponseEntity<>(notes, HttpStatus.OK);
	}

	// Test data
	@GetMapping("/notes/{testId}")
	public ResponseEntity<Optional<Note>> getOneTestData(@PathVariable("testId") Long testId) {
		Optional<Note> notes = noteRepository.findById(testId);
		return new ResponseEntity<>(notes, HttpStatus.OK);
	}

	// Get All Notes
//	@GetMapping("/notes")
//	public List<Note> getAllNotes() {
//		return noteRepository.findAll();
//	}

	// Create a new Note
	@PostMapping("/notes")
	public Note createNote(@Valid @RequestBody Note note) {
		return noteRepository.save(note);
	}

	// Get a Single Note
//	@GetMapping("/notes/{id}")
//	public Note getNoteById(@PathVariable(value = "id") Long noteId) {
//		return noteRepository.findById(noteId).orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
//	}

	// Update a Note
	@PutMapping("/notes/{id}")
	public Note updateNote(@PathVariable(value = "id") Long noteId, @Valid @RequestBody Note noteDetails) {

		Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

		note.setName(noteDetails.getName());

		Note updatedNote = noteRepository.save(note);
		return updatedNote;
	}

	// Delete a Note
	@DeleteMapping("/notes/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
		Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

		noteRepository.delete(note);

		return ResponseEntity.ok().build();
	}
}