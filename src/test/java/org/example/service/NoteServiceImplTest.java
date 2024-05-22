package org.example.service;

import org.example.dao.NoteDao;
import org.example.dao.NoteDaoImpl;
import org.example.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NoteServiceImplTest {

    private NoteDao noteDao;

    private NoteServiceImpl noteService;

    private NoteServiceImpl serviceImpl = new NoteServiceImpl(noteDao);


    @BeforeEach
    public void setup() {
        noteDao = Mockito.spy(NoteDao.class);
        noteService = new NoteServiceImpl(noteDao);
    }

    @DisplayName("Тестирование удаления заметки по невалидному ID")
    @Test
    void testRemoveNoteWithInvalidId() {
        long id = 26;
        Note note = new Note(26, "Birthday", Collections.singleton("dates"));
        assertThrows(NumberFormatException.class, () -> noteService.removeNoteById(Long.parseLong("двадцать шесть")));
    }

    @DisplayName("Тестирование удаления заметки по валидному ID")
    @Test
    void testRemoveNoteWithValidId() {
        long id = 88;
        Note note = new Note(id, "Важная информация", Collections.singleton("info"));
        Mockito.doNothing().when(noteDao).saveNote(note);
        noteDao.saveNote(note);

        Mockito.when(noteDao.deleteById(id)).thenReturn(true);
        assertDoesNotThrow(() -> noteService.removeNoteById(id));
    }

    @DisplayName("Тестирование создания заметки с валидно заполненными полями")
    @Test
    void testCreateNotes() {
        long id = 88;
        String text = "svbsb";
        Set<String> labels = new HashSet<>();
        labels.add("skjd");
        Note note = new Note(id, text, labels);

        Mockito.when(noteDao.findById(id)).thenReturn(note);
        noteService.createNote(text, labels);

        Note savedNote = noteDao.findById(id);
        assertEquals(note.getId(), savedNote.getId());
        assertEquals(note.getText(), savedNote.getText());
        System.out.println(note.getText() + " // " + savedNote.getText()); // это для собственной уверенности
    }

    @DisplayName("Тестирование создания заметки с невалидно заполненными полями")
    @Test
    void testCreateNotesWithInvalid() {
        long id = 88;
        String text = "f";
        Set<String> labels = new HashSet<>();
        labels.add("skjd");
        Note note = new Note(id, text, labels);
        assertThrows(IllegalArgumentException.class, () -> noteService.createNote(text, labels));
    }
}