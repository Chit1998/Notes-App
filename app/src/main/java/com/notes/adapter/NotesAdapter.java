package com.notes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.notes.AddNotesActivity;
import com.notes.R;
import com.notes.models.NotesModel;

import java.text.DateFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteVH> {

    Context context;
    RealmResults<NotesModel> list;

    public NotesAdapter(Context context, RealmResults<NotesModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotesAdapter.NoteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_note_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteVH holder, int position) {
        NotesModel model = list.get(position);

        holder.title.setText(model.getTitle());
        holder.des.setText(model.getDescription());
        String time = DateFormat.getDateTimeInstance().format(model.getCreateTime());
        holder.time.setText(time);

        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu menu = new PopupMenu(context,v);
            menu.getMenu().add("Edit").setOnMenuItemClickListener(item -> {
                context.startActivity(new Intent(context.getApplicationContext(), AddNotesActivity.class)
                        .putExtra("title", model.getTitle())
                        .putExtra("content",model.getDescription())
                        .putExtra("time", model.getCreateTime())
                        .putExtra("id", model.getUid()));
                return true;
            });
            menu.getMenu().add("Delete").setOnMenuItemClickListener(item -> {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                model.deleteFromRealm();
                realm.commitTransaction();
                Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();
                return true;
            });

            menu.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoteVH extends RecyclerView.ViewHolder {
        TextView title;
        TextView des;
        TextView time;

        public NoteVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleRV);
            des = itemView.findViewById(R.id.desRV);
            time = itemView.findViewById(R.id.timeRV);
        }
    }
}
