package org.acme.sample.controller;

import org.acme.sample.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.ws.rs.Path;
import org.acme.sample.model.NoteRepository;

@Component
@Path("/notes")
public class NoteController {

    @Autowired
    private NoteRepository NoteRepository;

    @Path("/create")
    public String create(String text) {
        String NoteId = "";
        try {
            Note Note = new Note(text);
            NoteRepository.save(Note);
            NoteId = String.valueOf(Note.getId());
        }
        catch (Exception ex) {
            return "Error creating the note: " + ex.toString();
        }
        return "Note succesfully created with id = " + NoteId;
    }

    @Path("/find")
    public String getByText(String text) {
        String NoteId = "";
        try {
            Note Note = NoteRepository.findByText(text);
            NoteId = String.valueOf(Note.getId());
        }
        catch (Exception ex) {
            return "Note not found";
        }
        return "The Note id is: " + NoteId;
    }

    @Path("/update")
    public String updateNote(long id, String text) {
        try {
            Note Note = NoteRepository.findOne(id);
            Note.setText(text);
            NoteRepository.save(Note);
        }
        catch (Exception ex) {
            return "Error updating the note: " + ex.toString();
        }
        return "Note succesfully updated!";
    }

}