package org.example;

import org.example.controller.Controller;
import org.example.dao.NoteDao;
import org.example.dao.NoteDaoImpl;
import org.example.service.NoteService;
import org.example.service.NoteServiceImpl;

import java.io.IOException;
import java.util.logging.*;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        setupLogger();

        NoteDao noteDao = new NoteDaoImpl();

        NoteService noteService = new NoteServiceImpl(noteDao);

        Controller controller = new Controller(noteService);

        controller.start();
    }

    private static void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("D:/idea/noteBook/note.log", true);
            fileHandler.setLevel(Level.FINE);
            fileHandler.setFormatter(new SimpleFormatter());

            Logger rootLogger = LogManager.getLogManager().getLogger("");

            rootLogger.setLevel(Level.FINE);

            rootLogger.addHandler(fileHandler);

            rootLogger.setUseParentHandlers(false);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Не удалось записать логи " + e.getMessage());
        }
    }
}