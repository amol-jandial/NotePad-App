package com.yepyuno.notepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yepyuno.notepad.Adapters.NotesListAdapter;
import com.yepyuno.notepad.Database.RoomDatabase;
import com.yepyuno.notepad.Models.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Note> notes = new ArrayList<>();
    RoomDatabase database;
    FloatingActionButton fab;
    SearchView searchView;
    Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycleView_notes);
        fab = findViewById(R.id.fap_insert);
        searchView = findViewById(R.id.searchView_home);

        database = RoomDatabase.getInstance(this);
        notes = database.mainDAO().getNotes();

        updateRecycler(notes);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotesTakerActvity.class);
                startActivityForResult(intent, 101);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<Note> filteredList = new ArrayList<>();
        for(Note singleNote: notes){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNote.getNote().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK){
                Note newNote = (Note) data.getSerializableExtra("note");
                database.mainDAO().insertNote(newNote);
                notes.clear();
                notes.addAll(database.mainDAO().getNotes());
                notesListAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                Note newNote = (Note) data.getSerializableExtra("note");
                database.mainDAO().updateNote(newNote.getId(), newNote.getTitle(), newNote.getNote());
                notes.clear();
                notes.addAll(database.mainDAO().getNotes());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Note> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);

    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Note note) {
            Intent intent = new Intent(MainActivity.this, NotesTakerActvity.class);
            intent.putExtra("oldNote", note);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Note note, CardView cardView) {
            selectedNote = new Note();
            selectedNote = note;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.popup_pin:
                if(selectedNote.isPinned()){
                    database.mainDAO().pin(selectedNote.getId(), false);
                    Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_LONG).show();
                }else{
                    database.mainDAO().pin(selectedNote.getId(), true);
                    Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_LONG).show();

                }
                notes.clear();
                notes.addAll(database.mainDAO().getNotes());
                notesListAdapter.notifyDataSetChanged();
                return true;

            case R.id.popup_delete:
                database.mainDAO().deleteNote(selectedNote);
                notes.remove(selectedNote);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_LONG).show();
                return true;

            default:
                return false;
        }

    }
}