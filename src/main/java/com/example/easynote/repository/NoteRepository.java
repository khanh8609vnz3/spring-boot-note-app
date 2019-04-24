package com.example.easynote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.easynote.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

	public final String getNoteQuery = "SELECT * FROM notes n, grouptable g "
			+ "WHERE (n.name = :name OR :name is null) "
			+ "AND (n.id = :id OR :id is null) "
			+ "AND (n.group_id = :groupId OR :groupId is null)"
			+ "AND (g.group_name = :groupName OR :groupName is null)";

	@Query(value = getNoteQuery, nativeQuery = true)
	List<Note> findAllNotesWithParams(String name, Long id, Long groupId, String groupName);

	List<Note> findAllByNameAndId(@Param("name") String name, @Param("id") Long id);
}
