package org.example.service;

import org.example.model.Note;

import java.util.List;
import java.util.Set;

public interface NoteService {

    String getHelp();

    void newNote();  //- создать новую заметку

    public List<Note> listNotes(Set<String> filters); //- выводит все заметки на экран

    void removeNote(); //- удаляет заметку

    void exportNote(); //- сохраняет все заметки в текстовый файл и выводит имя сохраненного файла

    void exit(); //- выход из приложения

}
