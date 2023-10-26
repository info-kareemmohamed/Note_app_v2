package com.example.notebook.databace;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notebook.Dao.Note_Dao;
import com.example.notebook.Entity.Note;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class}, version = 1)
public abstract class Databace extends RoomDatabase {
    public abstract Note_Dao dao();

    private static volatile Databace Instance;
    public static synchronized Databace getInstance(Context context) {
        if (Instance == null) {
            Instance = Room.databaseBuilder(context.getApplicationContext(), Databace.class, "Notebook").fallbackToDestructiveMigration().build();
        }

        return Instance;
    }


}
