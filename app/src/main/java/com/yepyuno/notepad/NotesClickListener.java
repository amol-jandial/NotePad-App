package com.yepyuno.notepad;

import androidx.cardview.widget.CardView;

import com.yepyuno.notepad.Models.Note;

public interface NotesClickListener {

    void onClick(Note note);

    void onLongClick(Note note, CardView cardView);

}
