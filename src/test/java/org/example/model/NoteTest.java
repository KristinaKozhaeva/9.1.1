package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @DisplayName("Тестирование создания уникальных ID")
    @Test
    public void testGenerateUniqueId() {
        Set<Long> uniqueId = new HashSet<>();
        for (int i = 0; i < 108; i++) {
            long newId = Note.generateId();
            assertTrue(uniqueId.add(newId));
        }
    }
}