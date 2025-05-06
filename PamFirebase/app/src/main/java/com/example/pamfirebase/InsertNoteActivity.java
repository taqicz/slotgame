package com.example.pamfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InsertNoteActivity extends AppCompatActivity {

    private static final String TAG = "InsertNoteActivity";
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private EditText etTitle, etDescription;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private ArrayList<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://pamfirebase-4365c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("notes");

        // Initialize UI components
        initUI();

        // Check user authentication
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Anda harus login terlebih dahulu", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Set user info
        TextView tvEmail = findViewById(R.id.tv_email);
        TextView tvUid = findViewById(R.id.tv_uid);
        tvEmail.setText("Email: " + user.getEmail());
        tvUid.setText("UID: " + user.getUid());

        // Setup RecyclerView
        setupRecyclerView();

        // Setup button listeners
        setupButtonListeners();

        // Load data from Firebase
        loadNotesFromFirebase();
    }

    private void initUI() {
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        Button btnSubmit = findViewById(R.id.btn_submit);
        Button btnKeluar = findViewById(R.id.btn_keluar);
        recyclerView = findViewById(R.id.recycler_view_notes);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteList = new ArrayList<>();
        adapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupButtonListeners() {
        findViewById(R.id.btn_submit).setOnClickListener(v -> saveNote());
        findViewById(R.id.btn_keluar).setOnClickListener(v -> logoutUser());
    }

    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Judul tidak boleh kosong");
            etTitle.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            etDescription.setError("Deskripsi tidak boleh kosong");
            etDescription.requestFocus();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan catatan...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String id = databaseReference.push().getKey();
        Note note = new Note(title, description);
        note.setId(id);

        databaseReference.child(id).setValue(note)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Catatan berhasil disimpan", Toast.LENGTH_SHORT).show();
                    etTitle.setText("");
                    etDescription.setText("");
                    Log.d(TAG, "Note saved successfully with ID: " + id);
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Gagal menyimpan catatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to save note", e);
                });
    }

    private void loadNotesFromFirebase() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat catatan...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                noteList.clear();

                if (!snapshot.exists()) {
                    Toast.makeText(InsertNoteActivity.this,
                            "Belum ada catatan, tambahkan catatan baru!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DataSnapshot data : snapshot.getChildren()) {
                    Note note = data.getValue(Note.class);
                    if (note != null) {
                        note.setId(data.getKey());
                        noteList.add(note);
                        Log.d(TAG, "Loaded note: " + note.getTitle());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(InsertNoteActivity.this,
                        "Gagal memuat data: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to load notes", error.toException());
            }
        });
    }

    private void logoutUser() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Keluar...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth.signOut();
        progressDialog.dismiss();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}