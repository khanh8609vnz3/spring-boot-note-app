package com.example.easynote.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class NoteController {

	@Autowired
	NoteRepository noteRepository;

	private static final Logger log = LoggerFactory.getLogger(NoteController.class);

	// Say hello
	@GetMapping("/hello/{id}")
	public Note sayHello(@PathVariable("id") Long id) {
		Note note = noteRepository.findNoteById(id);
		return note;
	}

	// Get all note
	@GetMapping("/notes")
	public ResponseEntity<List<Note>> getAllNote(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "groupId", required = false) Long groupId,
			@RequestParam(value = "groupName", required = false) String groupName) {
		List<Note> notes = new ArrayList<>();
		if (name != null || id != null || groupId != null || groupName != null) {
			notes = noteRepository.findAllNotesWithParams(name, id, groupId, groupName);
//			notes = noteRepository.findAllByNameAndId(name, id);
		} else {
			notes = noteRepository.findAll();
		}

		return new ResponseEntity<>(notes, HttpStatus.OK);
	}

	// Get one note
	@GetMapping("/notes/{noteId}")
	public ResponseEntity<Note> getOneNotes(@PathVariable("noteId") Long noteId) {
		Note oneNote = noteRepository.findNoteById(noteId);
		return new ResponseEntity<>(oneNote, HttpStatus.OK);
	}

	// Create a new Note
	@PostMapping("/notes")
	public ResponseEntity<Note> createNote(@Valid @RequestBody Note note) {
		noteRepository.save(note);
		Note oneNote = noteRepository.findNoteById(note.getId());

//		noteRepository.insertNote(note.getName(), note.getGroup().getGroupId());

		return new ResponseEntity<>(oneNote, HttpStatus.OK);
	}

	// Update a Note
	@PutMapping("/notes/{id}")
	public Note updateNote(@PathVariable(value = "id") Long noteId, @Valid @RequestBody Note noteDetails) {
		Note note = noteRepository.findNoteById(noteId);
		note.setName(noteDetails.getName());
		note.setGroup(noteDetails.getGroup());

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