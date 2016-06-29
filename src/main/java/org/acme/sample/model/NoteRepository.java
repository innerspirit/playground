package org.acme.sample.model;

import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;

@Transactional
public interface NoteRepository extends CrudRepository<Note, Long> {

    public Note findByText(String text);

}