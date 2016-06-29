package org.acme.sample.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String text;

    // Public methods

    public Note() { }

    public Note(long id) {
        this.id = id;
    }

    public Note(String text) {
        setText(text);
    }

    public long getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }

}