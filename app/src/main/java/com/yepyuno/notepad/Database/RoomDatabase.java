package com.yepyuno.notepad.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.yepyuno.notepad.Models.Note;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static RoomDatabase database;
    private static String DatabaseName = "NotesApp";

    public synchronized static RoomDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDatabase.class, DatabaseName)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MainDAO mainDAO();

}
