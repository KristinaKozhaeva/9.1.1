package org.example.service;

import org.example.dao.NoteDao;
import org.example.model.Note;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class NoteServiceImpl implements NoteService {

    private final NoteDao noteDao;

    private static final Logger logger = Logger.getLogger(NoteServiceImpl.class.getName());

    public NoteServiceImpl(NoteDao noteDao) {
        this.noteDao = noteDao;
    }


    @Override
    public String getHelp() {
        logger.fine("Вызвана команда help");
        return "help - выводит на экран список доступных команд с их описанием\n" +
                "note-new  - создать новую заметку\n" +
                "note-list - выводит все заметки на экран\n" +
                "note-remove - удаляет заметку\n" +
                "note-export - сохраняет все заметки в текстовый файл и выводит имя сохраненного файла\n" +
                "exit - выход из приложения";
    }

    @Override
    public void newNote() {
        logger.fine("Вызвана команда note-new");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите заметку:");
        String text = scanner.nextLine();

        if (text == null || text.length() < 3) {
            logger.info("Текст заметки должен быть длиннее 3 символов, введено - " + text);
            System.out.println("Ошибка: текст заметки должен содержать не менее 3 символов.");
            return;
        }

        System.out.println("Добавить метки? Метки состоят из одного слова и могут содержать только буквы. Для добавления нескольких меток разделяйте слова пробелом:");
        String labelsInput = scanner.nextLine();
        Set<String> labels = Arrays.stream(labelsInput.split(" "))
                .map(String::toUpperCase)
                .filter(label -> Pattern.matches("^[A-Z]+$", label))
                .collect(Collectors.toSet());

        if (labels.isEmpty()) {
            logger.info("Метки введены некорректно, введено - " + labelsInput);
            System.out.println("Ошибка: метки должны состоять только из букв.");
            return;
        }

        long id = Note.generateId();
        Note note = new Note(id, text, labels);
        noteDao.saveNote(note);

        System.out.println("Заметка добавлена");
    }

    @Override
    public List<Note> listNotes(Set<String> filters) {
        logger.fine("Вызвана команда note-list");
        List<Note> notes = noteDao.findAll();
        if (filters == null || filters.isEmpty()) {
            return notes;
        }
        return notes.stream()
                .filter(note -> note.getLabels().stream().anyMatch(filters::contains))
                .collect(Collectors.toList());
    }

    @Override
    public void removeNote() {
        logger.fine("Вызвана команда note-remove");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите id удаляемой заметки:");
        String idInput = scanner.nextLine();

        long id;
        try {
            id = Long.parseLong(idInput);
        } catch (NumberFormatException e) {
            logger.info("Некорректный id - " + idInput);
            System.out.println("Ошибка: id должен быть числом.");
            return;
        }

        Note note = noteDao.findById(id);
        if (note == null) {
            System.out.println("Заметка не найдена.");
        } else {
            noteDao.delete(id);
            System.out.println("Заметка удалена.");
        }
    }

    @Override
    public void exportNote() {
        logger.fine("Вызвана команда note-export");
        List<Note> notes = noteDao.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String filename = "notes_" + timestamp + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Note note : notes) {
                writer.write(note.toString());
            }
            System.out.println("Заметки сохранены в файл: " + filename);
        } catch (IOException e) {
            logger.warning("Ошибка при сохранении заметок: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void exit() {
        logger.fine("Вызвана команда exit");
        System.out.println("Программа завершена.");
        System.exit(0);
    }
}



