package com.example.roomreservation.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomreservation.R;
import com.example.roomreservation.api.ApiClient;
import com.example.roomreservation.api.ApiService;
import com.example.roomreservation.db.models.User;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editEmail, editPassword, editConfirmPassword;
    private Button btnRegister;
    private ProgressBar progressBar;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi komponen UI
        editEmail = findViewById(R.id.edit_register_email);
        editPassword = findViewById(R.id.edit_register_password);
        editConfirmPassword = findViewById(R.id.edit_register_confirm_password);
        btnRegister = findViewById(R.id.btn_register_submit);
        progressBar = findViewById(R.id.progress_bar);

        // Inisialisasi ApiService
        apiService = ApiClient.getClient().create(ApiService.class);

        // Tombol Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    /**
     * Fungsi untuk melakukan registrasi pengguna
     */
    private void registerUser() {
        // Ambil input dari pengguna
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Email tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Password tidak boleh kosong");
            return;
        }

        if (!password.equals(confirmPassword)) {
            editConfirmPassword.setError("Konfirmasi password tidak cocok");
            return;
        }

        // Tampilkan progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Buat objek User untuk dikirim ke server
        User user = new User(email, password);

        // Panggil API untuk registrasi
        Call<JSONObject> call = apiService.registerUser(user);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show();
                    finish(); // Tutup aktivitas setelah sukses
                } else {
                    Toast.makeText(RegisterActivity.this, "Registrasi gagal. Coba lagi nanti.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}