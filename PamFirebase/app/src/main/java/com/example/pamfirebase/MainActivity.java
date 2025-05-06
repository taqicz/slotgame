package com.example.pamfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnAutoLogin;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, InsertNoteActivity.class));
            finish();
            return;
        }

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnAutoLogin = findViewById(R.id.btn_auto_login);

        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            loginUser(email, password);
        });

        btnAutoLogin.setOnClickListener(view -> {
            etEmail.setText("test@example.com");
            etPassword.setText("password123");
            loginUser("test@example.com", "password123");
        });
    }

    private void loginUser(String email, String password) {
        Log.d(TAG, "Attempting to login with email: " + email);

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email wajib diisi");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password wajib diisi");
            etPassword.requestFocus();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang login...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        Log.d(TAG, "Login successful, checking database connection");
                        checkDatabaseConnection();
                    } else {
                        String errorMessage = task.getException() != null ?
                                task.getException().getMessage() : "Login gagal";
                        Log.e(TAG, "Login failed: " + errorMessage);
                        Toast.makeText(MainActivity.this,
                                "Login gagal: " + errorMessage,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkDatabaseConnection() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memeriksa koneksi database...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference testRef = FirebaseDatabase.getInstance().getReference("connection_test");
        testRef.setValue(new Date().toString())
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "Database connection successful");
                    Toast.makeText(MainActivity.this,
                            "Login berhasil",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, InsertNoteActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.e(TAG, "Database connection failed", e);
                    Toast.makeText(MainActivity.this,
                            "Login berhasil tapi gagal terhubung ke database",
                            Toast.LENGTH_LONG).show();
                });
    }
}