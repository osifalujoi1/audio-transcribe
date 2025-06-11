package com.audio.transcribe.repositories;

import com.audio.transcribe.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
