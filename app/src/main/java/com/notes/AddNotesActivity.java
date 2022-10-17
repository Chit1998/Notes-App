package com.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.notes.models.NotesModel;

import java.util.UUID;

import io.realm.Realm;

public class AddNotesActivity extends AppCompatActivity {

    private EditText eTitle;
    private EditText eDescription;
    private FloatingActionButton saveFab;
    private String title, description, noteId, createTime;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        eTitle = findViewById(R.id.eTitle);
        eDescription = findViewById(R.id.eDescription);
        saveFab = findViewById(R.id.saveFAB);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("content");
        noteId = getIntent().getStringExtra("id");
        createTime = getIntent().getStringExtra("time");

        if (title != null && description != null && noteId != null){
            eTitle.setText(title);
            eDescription.setText(description);
            saveFab.setOnClickListener(v -> {
                String noteTitle = eTitle.getText().toString();
                String noteDes = eDescription.getText().toString();
                long createTime = System.currentTimeMillis();
                realm.beginTransaction();
                NotesModel model = realm.where(NotesModel.class).equalTo("uid", noteId).findFirst();
                model.setTitle(noteTitle);
                model.setDescription(noteDes);
                model.setCreateTime(createTime);
                realm.commitTransaction();
                Toast.makeText(AddNotesActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

        }else {
            saveFab.setOnClickListener(v -> {
                String noteTitle = eTitle.getText().toString();
                String noteDes = eDescription.getText().toString();
                long createTime = System.currentTimeMillis();
                realm.beginTransaction();
                NotesModel model = realm.createObject(NotesModel.class,UUID.randomUUID().toString());
                model.setTitle(noteTitle);
                model.setDescription(noteDes);
                model.setCreateTime(createTime);
                realm.commitTransaction();
                Toast.makeText(AddNotesActivity.this, "save successfully..!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}