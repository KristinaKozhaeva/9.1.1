package org.example;

import org.example.controller.Controller;
import org.example.dao.NoteDao;
import org.example.dao.NoteDaoImpl;
import org.example.service.NoteService;
import org.example.service.NoteServiceImpl;

public class Main {
    public static void main(String[] args) {
        
        NoteDao noteDao = new NoteDaoImpl();

        NoteService noteService = new NoteServiceImpl(noteDao);

        Controller controller = new Controller(noteService);

        controller.start();
    }
}