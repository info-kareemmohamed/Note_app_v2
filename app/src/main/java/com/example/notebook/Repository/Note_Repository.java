package com.example.notebook.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.example.notebook.Dao.Note_Dao;
import com.example.notebook.Entity.Note;
import com.example.notebook.databace.Databace;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Note_Repository {
    private Databace Instance;
    private Note_Dao dao;

    public Note_Repository(Application application) {
        Instance = Databace.getInstance(application);
        dao = Instance.dao();
    }


    public void add_note(Note... note) {
        dao.add_note(note).subscribeOn(Schedulers.io()).subscribe();
    }

    public void update_note(Note... note) {
        dao.update_note(note)
                .subscribeOn(Schedulers.io()).subscribe();
    }

    public void delete_note(Note... note) {
        dao.delete_note(note)
                .subscribeOn(Schedulers.io()).subscribe();


    }

    public LiveData<List<Note>> get_all_notes() {
        return dao.get_all_notes();

    }

    public void delete_note(long id) {
        dao.delete_note(id)
                .subscribeOn(Schedulers.io()).subscribe();
    }


    public void update_record(long id, String record) {
        dao.update_record(id, record)
                .subscribeOn(Schedulers.io()).subscribe();
    }


    public void update_NoteState(long id, String description, String title, String data, String record, long recordTime, int color) {

        dao.update_NoteState(id, description, title, data, record, recordTime, color)
                .subscribeOn(Schedulers.io()).subscribe();

    }

    public void update_Favourite(long id, boolean favourite) {

        dao.update_Favourite(id, favourite)
                .subscribeOn(Schedulers.io()).subscribe();
    }


    public void delete(long id) {
        dao.delete(id)
                .subscribeOn(Schedulers.io()).subscribe();

    }

    public void update_Archived(long id, boolean archived) {
        dao.update_Archived(id, archived)
                .subscribeOn(Schedulers.io()).subscribe();
    }

    public LiveData<List<Note>> get_all_notes_Favourites() {
        return dao.get_all_notes_Favourites();
    }

    public LiveData<List<Note>> get_all_notes_Archived() {
        return dao.get_all_notes_Archived();

    }

    public LiveData<List<Note>> get_all_notes_not_Archived() {

        return dao.get_all_notes_not_Archived();
    }

    public void recovery(long id) {
        dao.recovery(id)
                .subscribeOn(Schedulers.io()).subscribe();

    }

}