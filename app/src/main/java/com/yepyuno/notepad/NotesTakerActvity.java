package com.yepyuno.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yepyuno.notepad.Models.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActvity extends AppCompatActivity {

    EditText title, note;
    ImageView save;
    Note notes;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker_actvity);

        save = findViewById(R.id.imageView_save);
        title = findViewById(R.id.editText_title);
        note = findViewById(R.id.editText_note);

        notes = new Note();
        try{
            notes = (Note) getIntent().getSerializableExtra("oldNote");
            title.setText(notes.getTitle());
            note.setText(notes.getNote());
            isOldNote = true;
        }catch(Exception e){
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Currenttitle = title.getText().toString();
                String description = note.getText().toString();

                if(description.isEmpty()){
                    Toast.makeText(NotesTakerActvity.this, "Add notes", Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();

                if(!isOldNote){
                    notes = new Note();
                }

                notes.setTitle(Currenttitle);
                notes.setNote(description);
                notes.setDateTime(formatter.format(date));

                Intent intent = new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}