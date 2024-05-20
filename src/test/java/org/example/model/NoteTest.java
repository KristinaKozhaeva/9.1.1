package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void testUniqueGenerateId() {
        long id1 = Note.generateId();
        long id2 = Note.generateId();
        long id3 = Note.generateId();
        assertAll("id", () -> assertEquals(1, id1),
                () -> assertEquals(2, id2),
                () -> assertEquals(3, id3)
        );
    }


    @Test
    void testSequentialGenerateId() {
        for (int i = 1; i <= 100; i++) {
            assertEquals(i, Note.generateId());
        }
    }

}