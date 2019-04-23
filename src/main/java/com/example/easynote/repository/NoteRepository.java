package com.example.easynote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.easynote.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

}
