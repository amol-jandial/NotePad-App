package com.yepyuno.notepad.Database;

import static androidx.room.OnConflictStrategy.REPLACE;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.yepyuno.notepad.Models.Note;

import java.util.List;

@Dao
public interface MainDAO {

    @Insert(onConflict = REPLACE)
    void insertNote(Note note);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Note> getNotes();

    @Query("UPDATE notes SET title = :title, note = :note WHERE id = :id")
    void updateNote(int id, String title, String note);

    @Delete
    void deleteNote(Note note);

    @Query("UPDATE notes SET isPinned = :pin WHERE id = :id")
    void pin(int id, boolean pin);

}
