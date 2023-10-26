package com.example.notebook.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.example.notebook.Entity.Note;
import com.example.notebook.Repository.Note_Repository;

import java.util.List;

public class NoteState_Viewmodel extends AndroidViewModel {
    Note_Repository repository;

    public NoteState_Viewmodel(@NonNull Application application) {
        super(application);
        repository = new Note_Repository(application);
    }

    public void add_note(Note... note) {
        repository.add_note(note);
    }

    public void update_note(Note... note) {
        repository.update_note(note);
    }


    public void update_record(long id, String record) {
        repository.update_record(id, record);
    }

    public void update_NoteState(long id, String description, String title, String data, String record, long recordTime, int color) {

        repository.update_NoteState(id, description, title, data, record, recordTime, color);
    }

    public void update_Archived(long id, boolean archived) {
        repository.update_Archived(id, archived);
    }

    public void delete(long id){
        repository.delete(id);
    }




}