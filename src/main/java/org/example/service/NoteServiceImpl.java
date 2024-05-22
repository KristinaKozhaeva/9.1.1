package org.example.service;

import org.example.dao.NoteDao;
import org.example.model.Note;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class NoteServiceImpl implements NoteService {

    private final NoteDao noteDao;

    private static final Logger LOGGER = Logger.getLogger(NoteServiceImpl.class.getName());

    private static final String TEXT_SAVE_MESSEGE = "Заметка добавлена";

    private static final String TEXT_HELP_MESSEGE = "help - выводит на экран список доступных команд с их описанием\n" +
            "note-new  - создать новую заметку\n" +
            "note-list - выводит все заметки на экран\n" +
            "note-remove - удаляет заметку\n" +
            "note-export - сохраняет все заметки в текстовый файл и выводит имя сохраненного файла\n" +
            "exit - выход из приложения";

    private static final String export = "Заметки сохранены в файл: ";

    private static final String EXEPTION_TEXT_VALIDATE_MESSAGE = "Текст заметки должен быть длиннее 3 символов, введено - ";

    public NoteServiceImpl(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public String getHelp() {
        LOGGER.fine("Вызвана команда help");
        StringBuilder sb = new StringBuilder();
        sb.append(TEXT_HELP_MESSEGE);
        System.out.println(sb);
        return sb.toString();

    }


    @Override
    public void createNote(String text, Set<String> labels) {
        LOGGER.fine("Вызвана команда note-new");
        validateNoteText(text);
        validateLabels(labels);
        long id = Note.generateId();
        Note note = new Note(id, text, labels);
        noteDao.saveNote(note);

        System.out.println(TEXT_SAVE_MESSEGE);
        LOGGER.fine(TEXT_SAVE_MESSEGE);
    }

    @Override
    public List<Note> getlistNotes(Set<String> filters) {
        LOGGER.fine("Вызвана команда note-list");
        List<Note> notes = noteDao.findAll();
        if (filters == null || filters.isEmpty()) {
            return notes;
        }
        return notes.stream()
                .filter(note -> note.getLabels().stream().anyMatch(filters::contains))
                .collect(Collectors.toList());
    }

    @Override
    public void removeNoteById(long id) {
        LOGGER.fine("Вызвана команда note-remove");
        if (noteDao.deleteById(id)) {
            System.out.println("Заметка удалена");

            StringBuilder logMessage = new StringBuilder();
            logMessage.append("Заметка с ID ").append(id).append(" удалена");

            LOGGER.log(Level.FINE, logMessage.toString());
        } else {
            System.out.println("Заметка не найдена");

            StringBuilder logMessage = new StringBuilder();
            logMessage.append("Заметка с ID ").append(id).append(" не найдена");

            LOGGER.log(Level.WARNING, logMessage.toString());
        }
    }

    @Override
    public void exportNote() {
        LOGGER.fine("Вызвана команда note-export");
        List<Note> notes = noteDao.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH-mm-ss"); // исправить
        String timestamp = LocalDateTime.now().format(formatter);
        String filename = "notes_" + timestamp + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Note note : notes) {
                writer.write(note.toString());
            }
            System.out.println(export + filename);
            LOGGER.fine(export + filename);
        } catch (IOException e) {
            LOGGER.warning("Ошибка при сохранении заметок: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void exit() {
        LOGGER.log(Level.FINE, "Вызвана команда exit ");
        System.out.println("Программа завершена.");
        System.exit(0);
    }


    public void validateNoteText(String text) {
        if (text == null || text.length() < 3) {
            throw new IllegalArgumentException(EXEPTION_TEXT_VALIDATE_MESSAGE + text.length());
        }
    }

    public void validateLabels(Set<String> labels) {
        Pattern pattern = Pattern.compile("^[a-zA-Zа-яА-Я]+$");
        for (String label : labels) {
            if (!pattern.matcher(label).matches()) {
                throw new IllegalArgumentException("Метки должны состоять только из букв");
            }
        }
    }
}