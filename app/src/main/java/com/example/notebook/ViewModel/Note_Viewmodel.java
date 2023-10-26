package com.example.notebook.ViewModel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notebook.Entity.Note;
import com.example.notebook.Repository.Note_Repository;

import java.util.List;

public class Note_Viewmodel extends AndroidViewModel {
    private Note_Repository repository;


    public Note_Viewmodel(@NonNull Application application) {
        super(application);
        repository = new Note_Repository(application);
    }


    public void add_note(Note... note) {
        repository.add_note(note);
    }

    public void update_note(Note... note) {
        repository.update_note(note);
    }

    public void delete_note(Note... note) {
        repository.delete_note(note);


    }

    public LiveData<List<Note>> get_all_notes() {
        return repository.get_all_notes();

    }

    public void delete_note(long id) {
        repository.delete_note(id);


    }

    public void update_Favourite(long id, boolean favourite) {
        repository.update_Favourite(id, favourite);

    }

    public LiveData<List<Note>> get_all_notes_Favourites() {
        return repository.get_all_notes_Favourites();
    }

    public LiveData<List<Note>> get_all_notes_Archived() {
        return repository.get_all_notes_Archived();
    }

    public void delete(long id) {
        repository.delete(id);
    }

    public LiveData<List<Note>> get_all_notes_not_Archived() {

        return repository.get_all_notes_not_Archived();
    }

    public void recovery(long id) {

        repository.recovery(id);
    }

}
