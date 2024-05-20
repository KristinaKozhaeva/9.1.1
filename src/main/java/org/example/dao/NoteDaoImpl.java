package org.example.dao;

import org.example.model.Note;

import java.util.*;

public class NoteDaoImpl implements NoteDao{
    private final Map<Long, Note> notes = new HashMap<>();

    @Override
    public void saveNote(Note note) {
        notes.put(note.getId(), note);
    }

    @Override
    public List<Note> findAll() {
        return new ArrayList<>(notes.values());
    }

    @Override
    public Note findById(long id) {
        return notes.get(id);
    }

    @Override
    public void delete(long id) {
        notes.remove(id);
    }
}
