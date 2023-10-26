package com.example.notebook.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notebook.Entity.Note;
import io.reactivex.rxjava3.core.Completable;

import java.util.List;
@Dao
public interface Note_Dao {


    @Insert
    public Completable add_note(Note... note);

    @Update
    public Completable update_note(Note... note);

    @Delete
    public Completable delete_note(Note... note);

    @Query("select * from Note")
    public LiveData<List<Note>> get_all_notes();

    @Query("select * from Note where Archived=0")
    public LiveData<List<Note>> get_all_notes_not_Archived();


    @Query("select * from Note where Favourites=1")
    public LiveData<List<Note>> get_all_notes_Favourites();

    @Query("select * from Note where Archived=1")
    public LiveData<List<Note>> get_all_notes_Archived();

    @Query("delete  from Note where Id=:id")
    public Completable delete_note(long id);

    @Query("update   Note set Archived=1 where Id=:id")
    public Completable delete(long id);

    @Query("update   Note set Archived=0 where Id=:id")
    public Completable recovery(long id);

    @Query("update   Note set record=:record where Id=:id")
    public Completable update_record(long id, String record);

    @Query("update   Note set Favourites=:favourite where Id=:id")
    public Completable update_Favourite(long id, boolean favourite);


    @Query("update Note set record=:record, RecordTime=:recordTime, Color=:color, Description=:description, Title=:title, Date=:data where Id=:id")
   public Completable update_NoteState(long id, String description, String title, String data, String record, long recordTime, int color);

    @Query("update   Note set Archived=:archived where Id=:id")
    public Completable update_Archived(long id, boolean archived);
}
