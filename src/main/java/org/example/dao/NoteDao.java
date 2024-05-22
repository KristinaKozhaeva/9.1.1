package org.example.dao;

import org.example.model.Note;

import java.util.*;

public interface NoteDao {

    void saveNote(Note note);

    public List<Note> findAll();

    public Note findById(long id);

    boolean deleteById(long id);
}

