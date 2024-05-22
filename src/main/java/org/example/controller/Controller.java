package org.example.controller;

import org.example.Main;
import org.example.service.NoteService;
import org.example.model.Note;
import org.example.service.NoteServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static java.lang.Character.isAlphabetic;

public class Controller {

    private final NoteService noteService;

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public Controller(NoteService noteService) {
        this.noteService = noteService;
    }

    private static final String START_MESSAGE = "Это Ваша записная книжка. Вот список доступных команд: help, note-new, note-list, note-remove, note-export, exit.";

    private static final String LABELS_MESSAGE = "Добавить метки? Метки состоят из одного слова и могут содержать только буквы. Для добавления нескольких меток разделяйте слова пробелом.";

    private static final String FILTERS_LABELS_MESSAGE = "Введите метки, чтобы отобразить определенные заметки или оставьте пустым для отображения всех заметок:";

    private static final String VALIDATE_MESSAGE = "Метки должны содержать только буквы: ";

    private static final String EXEPTION_TEXT_VALIDATE_MESSAGE = "Текст заметки должен быть длиннее 3 символов, введено - ";

    private static final String EXEPTION_LABEL_VALIDATE_MESSAGE = "Метки должны состоять только из букв";

    private static final String EXEPTION_ID_MESSAGE = "Ошибка: ID должно быть числом";

    public void start() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(START_MESSAGE);

        while (true) {
            try {
                System.out.print("> ");
                String command = bf.readLine().trim().toLowerCase();
                switch (command) {
                    case "help":
                        getHelp();
                        break;
                    case "note-new":
                        createNote();
                        break;
                    case "note-list":
                        getlistNotes();
                        break;
                    case "note-remove":
                        removeNoteById();
                        break;
                    case "note-export":
                        exportNote();
                        break;
                    case "exit":
                        exit();
                        return;
                    default:
                        System.out.println("Команда не найдена");
                        break;
                }
            } catch (IOException e) {
                System.err.println("Ошибка ввода/вывода: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }

    private void getHelp() {
        try {
            noteService.getHelp();
            LOGGER.fine("Вызвана команда help");
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Ошибка ");
        }
    }


    public void createNote() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите заметку: ");
        String text = scanner.nextLine().trim();
        LOGGER.fine("Создание заметки с текстом: " + text);
        try {
            validateNoteText(text);

            System.out.println(LABELS_MESSAGE);
            String labelsInput = scanner.nextLine().trim();
            LOGGER.fine("Метки " + labelsInput);

            Set<String> labels = new HashSet<>(Arrays.asList(labelsInput.split("\\s+")));
            validateLabels(labels);

            noteService.createNote(text, labels);
            LOGGER.fine("Заметка создана успешно");
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Ошибка при создании заметки: ", e);
            throw e;
        }
    }

    public void getlistNotes() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(FILTERS_LABELS_MESSAGE);
        String labelLine = scanner.nextLine().trim();
        LOGGER.fine("Фильтрация заметок по меткам: " + labelLine);

        Set<String> filters = new HashSet<>();
        if (!labelLine.isEmpty()) {
            String[] labelArray = labelLine.split("\\s+");
            for (String label : labelArray) {
                if (!isAlphabetic(Integer.parseInt(label))) {
                    LOGGER.warning(VALIDATE_MESSAGE + label);
                    System.out.println(VALIDATE_MESSAGE + label);
                }
                filters.add(label.toUpperCase());
            }
        }

        List<Note> notes = noteService.getlistNotes(filters);
        if (notes.isEmpty()) {
            System.out.println("Заметки не найдены.");
            LOGGER.fine("Заметки не найдены");
        } else {
            notes.forEach(System.out::println);
            LOGGER.fine("Список заметок");
        }
    }

    public void removeNoteById() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ID удаляемой заметки: ");
        try {
            long id = Long.parseLong(scanner.nextLine().trim());
            noteService.removeNoteById(id);
            LOGGER.fine("Заметка удалена");
        } catch (NumberFormatException e) {
            System.out.println(EXEPTION_ID_MESSAGE);
            LOGGER.warning(EXEPTION_ID_MESSAGE);
        }
    }

    public void exportNote() {
        noteService.exportNote();
        LOGGER.fine("Экспорт заметок");
    }

    public void exit() {
        noteService.exit();
        LOGGER.fine("Приложение закрыто");
    }

    public void validateNoteText(String text) {
        if (text == null || text.length() < 3) {
            throw new IllegalArgumentException(EXEPTION_TEXT_VALIDATE_MESSAGE + text.length());
        }
    }

    public void validateLabels(Set<String> labels) {
        Pattern pattern = Pattern.compile("^[a-zA-Zа-яА-Я]+$");
        for (String label : labels) {
            //   System.out.println("Проверка метки: " + label); // эта строчка было нужна для проверки почему тест не срабатывал
            if (!pattern.matcher(label).matches()) {
                //      System.out.println("Невалидная метка: " + label); // эта строчка было нужна для проверки почему тест не срабатывал
                throw new IllegalArgumentException(EXEPTION_LABEL_VALIDATE_MESSAGE);
            }
        }
    }

}