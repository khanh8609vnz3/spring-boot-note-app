package com.example.easynote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.easynote.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

	public final String getNoteQuery = "SELECT * FROM notes n, grouptable g "
			+ "WHERE (n.name = :name OR :name is null) "
			+ "AND (n.id = :id OR :id is null) "
			+ "AND (n.group_id = :groupId OR :groupId is null)"
			+ "AND (g.group_name = :groupName OR :groupName is null)";

	public final String getOneNoteQuery = "SELECT * FROM notes n, grouptable g "
			+ "WHERE (n.id = :id AND n.group_id = g.group_id)";

	public final String insertNoteQuery = "INSERT INTO notes(name, group_id) VALUES (:name,:groupId)";

	@Query(value = getNoteQuery, nativeQuery = true)
	public List<Note> findAllNotesWithParams(String name, Long id, Long groupId, String groupName);

	public List<Note> findAllByNameAndId(@Param("name") String name, @Param("id") Long id);

	@Query(value = getOneNoteQuery, nativeQuery = true)
	public Note findNoteById(Long id);

	@Modifying
	@Query(value = insertNoteQuery, nativeQuery = true)
	@Transactional
	public void insertNote(String name, Long groupId);

}
