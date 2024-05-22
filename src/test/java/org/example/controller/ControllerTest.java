package org.example.controller;

import org.example.dao.NoteDao;
import org.example.model.Note;
import org.example.service.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ControllerTest {

    NoteServiceImpl noteService = Mockito.mock(NoteServiceImpl.class);

    Controller controller = new Controller(noteService);

    Controller spyService = Mockito.spy(controller);

    @DisplayName("Тестирование валидации текста заметки с невалидными данными")
    @Test
    public void testValidateNoteTextWithInvalid() {
        String[] invalidText = {"ап", ""};
        for (String text : invalidText) {
            assertThrows(IllegalArgumentException.class, () -> spyService.validateNoteText(text));
        }
    }

    @DisplayName("Тестирование валидации текста заметки с валидными данными")
    @Test
    public void testValidateNoteTextWithValid() {
        String[] invalidText = {"sjdkvnv", "privet333", "123"};
        for (String text : invalidText) {
            assertDoesNotThrow(() -> spyService.validateNoteText(text));
        }
    }

    @DisplayName("Тестирование валидации меток заметки с валидными данными")
    @Test
    public void testValidateLabelsWithValid() {
        Set<String> labels = new HashSet<>();
        labels.add("Job");
        labels.add("Информация");
        labels.add("fabric details");
        assertDoesNotThrow(() -> spyService.validateNoteText(labels.toString()));

    }

    @DisplayName("Тестирование валидации меток заметки с невалидными данными")
    @Test
    public void testValidateLabelsWithInalid() {
        Set<String> labels = new HashSet<>();
        labels.add("л");
        labels.add("");
        labels.add("12");
        assertThrows(IllegalArgumentException.class, () -> spyService.validateLabels(Collections.singleton(labels.toString())));

    }
}
