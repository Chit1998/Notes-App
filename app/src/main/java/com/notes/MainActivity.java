package com.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.notes.adapter.NotesAdapter;
import com.notes.models.NotesModel;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton noteFAB;
    private RecyclerView rvNotes;
    private NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noteFAB = findViewById(R.id.noteFAB);
        rvNotes = findViewById(R.id.notesRecyclerView);

        noteFAB.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AddNotesActivity.class)));

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NotesModel> list = realm.where(NotesModel.class).findAll().sort("createTime", Sort.DESCENDING);
        rvNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new NotesAdapter(this, list);
        rvNotes.setAdapter(adapter);

        list.addChangeListener(notesModels -> adapter.notifyDataSetChanged());

    }
}