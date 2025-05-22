package com.example.roomreservation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomreservation.MainActivity;
import com.example.roomreservation.R;
import com.example.roomreservation.api.ApiClient;
import com.example.roomreservation.api.ApiService;
import com.example.roomreservation.utils.SessionManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin, btnRegister;
    private ProgressBar progressBar;

    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi komponen UI
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progress_bar);

        // Inisialisasi session manager dan API service
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        // Jika pengguna sudah login, langsung pindah ke MainActivity
        if (sessionManager.isLoggedIn()) {
            navigateToMain();
        }

        // Tombol Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Tombol Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegister();
            }
        });
    }

    /**
     * Fungsi untuk melakukan autentikasi pengguna
     */
    private void loginUser() {
        // Ambil input dari pengguna
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Email tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Password tidak boleh kosong");
            return;
        }

        // Tampilkan progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Panggil API untuk login
        Call<JSONObject> call = apiService.loginUser(email, password);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Dapatkan token dari respon
                        JSONObject jsonResponse = new JSONObject(response.body().toString());
                        String token = jsonResponse.getString("token");

                        // Simpan token ke session
                        sessionManager.saveAuthToken(token);

                        // Pindah ke MainActivity
                        navigateToMain();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Kesalahan parsing data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login gagal. Cek kembali email dan password Anda.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fungsi untuk navigasi ke MainActivity setelah login berhasil
     */
    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Fungsi untuk navigasi ke RegisterActivity
     */
    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}


