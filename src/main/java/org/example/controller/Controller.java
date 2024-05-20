package org.example.controller;

import org.example.service.NoteService;
import org.example.model.Note;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static java.lang.Character.isAlphabetic;

public class Controller {
    private final NoteService noteService;

    public Controller(NoteService noteService) {
        this.noteService = noteService;
    }

    public void start() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Это Ваша записная книжка. Вот список доступных команд: help, note-new, note-list, note-remove, note-export, exit.");

        while (true) {
            try {
                System.out.print("> ");
                String command = bf.readLine().trim().toLowerCase();
                switch (command) {
                    case "help":
                        System.out.println(noteService.getHelp());
                        break;
                    case "note-new":
                        handleNewNote();
                        break;
                    case "note-list":
                        handleListNotes();
                        break;
                    case "note-remove":
                        handleRemoveNote();
                        break;
                    case "note-export":
                        noteService.exportNote();
                        break;
                    case "exit":
                        noteService.exit();
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

    private void handleNewNote() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите заметку:");
        String text = scanner.nextLine().trim();

        if (text.length() < 3) {
            System.out.println("Заметка должна содержать не менее 3 символов");
            return;
        }

        System.out.println("Добавить метки? Метки состоят из одного слова и могут содержать только буквы. Для добавления нескольких меток разделяйте слова пробелом:");
        String labelLine = scanner.nextLine().trim();

        Set<String> labels = new HashSet<>();
        if (!labelLine.isEmpty()) {
            String[] labelArray = labelLine.split("\\s+");
            for (String label : labelArray) {
                if (!isAlphabetic(String.valueOf(Integer.parseInt(label)))) {
                    System.out.println("Метки должны содержать только буквы: " + label);
                    return;
                }
                labels.add(label.toUpperCase());
            }
        }

        noteService.newNote();
        System.out.println("Заметка добавлена.");
    }

    private void handleListNotes() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите метки, чтобы отобразить определенные заметки или оставьте пустым для отображения всех заметок:");
        String labelLine = scanner.nextLine().trim();

        Set<String> filters = new HashSet<>();
        if (!labelLine.isEmpty()) {
            String[] labelArray = labelLine.split("\\s+");
            for (String label : labelArray) {
                if (!isAlphabetic(label)) {
                    System.out.println("Метки должны содержать только буквы: " + label);
                    return;
                }
                filters.add(label.toUpperCase());
            }
        }

        List<Note> notes = noteService.listNotes(filters);
        if (notes.isEmpty()) {
            System.out.println("Заметки не найдены.");
        } else {
            notes.forEach(System.out::println);
        }
    }

    private void handleRemoveNote() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите id удаляемой заметки:");
        String idStr = scanner.nextLine().trim();

        if (!isNumeric(idStr)) {
            System.out.println("ID должен быть числом.");
            return;
        }

        long id = Long.parseLong(idStr);
        noteService.removeNote();
        System.out.println("Заметка удалена");
    }

    private boolean isAlphabetic(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}