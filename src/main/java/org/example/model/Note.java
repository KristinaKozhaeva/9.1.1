package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Note {
    private static long idCounter = 0;

    private long id;
    private String text;
    private Set<String> labels;

    public Note(long id, String text, Set<String> labels) {
        this.id = generateId();
        this.text = text;
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "{" + id + "}#" + text + "\n" +
                "{" + labels.stream().map(String::toUpperCase).collect(Collectors.joining(";")) + "}" + "\n" +
                "===================\n";
    }

    public synchronized static long generateId() {
        return ++idCounter;
    }
}


