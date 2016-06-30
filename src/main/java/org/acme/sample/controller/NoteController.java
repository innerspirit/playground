package org.acme.sample.controller;

import org.acme.sample.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.acme.sample.model.NoteRepository;

@Component
@Path("/notes")
public class NoteController {

    @Autowired
    private NoteRepository NoteRepository;

    @POST
    @Path("/")
    public String create(@FormParam("text") String text) {
        String NoteId = "";
        try {
            Note Note = new Note(text);
            NoteRepository.save(Note);
            NoteId = String.valueOf(Note.getId());
        }
        catch (Exception ex) {
            return "Error creating the note: " + ex.toString();
        }
        return "Note successfully created with id = " + NoteId;
    }

    @GET
    @Path("/find")
    public String getByText(@QueryParam("text") String text) {
        String NoteId = "";
        try {
            Note Note = NoteRepository.findByText(text);
            NoteId = String.valueOf(Note.getId());
        }
        catch (Exception ex) {
            return "Note not found";
        }
        return "The note id is: " + NoteId;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String updateNote(@PathParam("id") long id, @FormParam("text") String text) {
        try {
            Note Note = NoteRepository.findOne(id);
            Note.setText(text);
            NoteRepository.save(Note);
        }
        catch (Exception ex) {
            return "Error updating the note: " + ex.toString();
        }
        return "Note successfully updated!";
    }

}