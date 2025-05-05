package com.example.pamfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertNoteActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private TextView tvEmail, tvUid;
    private EditText etTitle, etDesc;
    private Button btnKeluar, btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        tvEmail = findViewById(R.id.tv_email);
        tvUid = findViewById(R.id.tv_uid);
        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_description);
        btnKeluar = findViewById(R.id.btn_keluar);
        btnSubmit = findViewById(R.id.btn_submit);

        btnKeluar.setOnClickListener(view -> logOut());
        btnSubmit.setOnClickListener(view -> submitData());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            tvEmail.setText(currentUser.getEmail());
            tvUid.setText(currentUser.getUid());
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            logOut();
        }
    }

    private void logOut() {
        mAuth.signOut();
        Intent intent = new Intent(InsertNoteActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private boolean validateForm() {
        boolean result = true;

        if (TextUtils.isEmpty(etTitle.getText().toString())) {
            etTitle.setError("Judul wajib diisi");
            result = false;
        }

        if (TextUtils.isEmpty(etDesc.getText().toString())) {
            etDesc.setError("Deskripsi wajib diisi");
            result = false;
        }

        return result;
    }

    private void submitData() {
        if (!validateForm()) return;

        String title = etTitle.getText().toString();
        String desc = etDesc.getText().toString();
        Note note = new Note(title, desc);

        databaseReference.child("notes").child(mAuth.getUid()).push()
                .setValue(note)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(InsertNoteActivity.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    etTitle.setText("");
                    etDesc.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(InsertNoteActivity.this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show());
    }
}
