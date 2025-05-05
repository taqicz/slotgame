package com.example.pamfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnAutoLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnAutoLogin = findViewById(R.id.btn_auto_login);

        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            login(email, password);
        });

        btnAutoLogin.setOnClickListener(view -> {
            String demoEmail = "test@example.com";
            String demoPass = "password123";
            login(demoEmail, demoPass);
        });
    }

    private void login(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email wajib diisi");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password wajib diisi");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, InsertNoteActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
