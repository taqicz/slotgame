package com.example.pamfirebase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> noteList;
    private Context context;
    private DatabaseReference databaseReference;

    public NoteAdapter(ArrayList<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
        this.databaseReference = FirebaseDatabase.getInstance("https://pamfirebase-4365c-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("notes");
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvDescription.setText(note.getDescription());

        holder.btnUpdate.setOnClickListener(v -> {
            showUpdateDialog(note);
        });

        holder.btnDelete.setOnClickListener(v -> {
            showDeleteConfirmationDialog(note);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    private void showUpdateDialog(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Catatan");

        // Inflate custom layout for dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update_note, null);
        builder.setView(view);

        EditText etUpdateTitle = view.findViewById(R.id.et_update_title);
        EditText etUpdateDescription = view.findViewById(R.id.et_update_description);

        // Set current values
        etUpdateTitle.setText(note.getTitle());
        etUpdateDescription.setText(note.getDescription());

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newTitle = etUpdateTitle.getText().toString().trim();
            String newDescription = etUpdateDescription.getText().toString().trim();

            if (newTitle.isEmpty()) {
                etUpdateTitle.setError("Judul tidak boleh kosong");
                etUpdateTitle.requestFocus();
                return;
            }

            if (newDescription.isEmpty()) {
                etUpdateDescription.setError("Deskripsi tidak boleh kosong");
                etUpdateDescription.requestFocus();
                return;
            }

            // Update note object
            note.setTitle(newTitle);
            note.setDescription(newDescription);

            // Update in Firebase
            databaseReference.child(note.getId()).setValue(note)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(context, "Catatan berhasil diupdate", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Gagal mengupdate catatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteConfirmationDialog(Note note) {
        new AlertDialog.Builder(context)
                .setTitle("Hapus Catatan")
                .setMessage("Apakah Anda yakin ingin menghapus catatan ini?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    databaseReference.child(note.getId()).removeValue()
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(context, "Catatan berhasil dihapus", Toast.LENGTH_SHORT).show();
                                noteList.remove(note);
                                notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Gagal menghapus catatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                .show();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        Button btnUpdate, btnDelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_note_title);
            tvDescription = itemView.findViewById(R.id.tv_note_description);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}