package com.example.easynote.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(NoteController.class);

	// Get all note
	@GetMapping("/notes")
	public ResponseEntity<List<Note>> getAllNote(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "id", required = false) Long id) {
		List<Note> notes = new ArrayList<>();
		if (name != null || id != null) {
			notes = noteRepository.findAllNotesWithParams(name, id);
//			notes = noteRepository.findAllByNameAndId(name, id);
		} else {
			notes = noteRepository.findAll();
		}
		log.info("Search result");
		log.info("-------------------------------");
		for (Note oneNote : notes) {
			log.info("id: " + oneNote.getId() + ", name: " + oneNote.getName());
		}

		return new ResponseEntity<>(notes, HttpStatus.OK);
	}

	// Get one note
	@GetMapping("/notes/{noteId}")
	public ResponseEntity<Optional<Note>> getOneNotes(@PathVariable("noteId") Long noteId) {
		Optional<Note> notes = noteRepository.findById(noteId);
		return new ResponseEntity<>(notes, HttpStatus.OK);
	}

	// Create a new Note
	@PostMapping("/notes")
	public Note createNote(@Valid @RequestBody Note note) {
		return noteRepository.save(note);
	}

	// Update a Note
	@PutMapping("/notes/{id}")
	public Note updateNote(@PathVariable(value = "id") Long noteId, @Valid @RequestBody Note noteDetails) {

		Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

		note.setName(noteDetails.getName());

		return noteRepository.save(note);

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