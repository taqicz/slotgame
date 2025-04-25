package com.example.praktikumlayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;




public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etEmail;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btn_login);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (email.equals("taqitalenta@gmail.com") && password.equals("235150701111026")) {
                    Toast.makeText(MainActivity.this,
                            "Berhasil Login ",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MrheadActivity.class);
                    intent.putExtra("EMAIL", email);
                    startActivity(intent);
                } else if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Email dan Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Login Gagal ! Email atau Password Salah", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
