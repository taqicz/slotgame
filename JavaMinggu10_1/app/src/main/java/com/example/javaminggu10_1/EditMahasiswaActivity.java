package com.example.javaminggu10_1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.javaminggu10_1.model.AddMahasiswaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMahasiswaActivity extends AppCompatActivity {
    private EditText edtNrp, edtNama, edtEmail, edtJurusan;
    private Button btnUpdate;
    private ProgressBar progressBar;

    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mahasiswa);

        edtNrp = findViewById(R.id.edtNrp);
        edtNama = findViewById(R.id.edtNama);
        edtEmail = findViewById(R.id.edtEmail);
        edtJurusan = findViewById(R.id.edtJurusan);
        btnUpdate = findViewById(R.id.btnUpdate);
        progressBar = findViewById(R.id.progressBar);

        // Ambil data dari intent
        id = getIntent().getStringExtra("id");
        String nrp = getIntent().getStringExtra("nrp");
        String nama = getIntent().getStringExtra("nama");
        String email = getIntent().getStringExtra("email");
        String jurusan = getIntent().getStringExtra("jurusan");

        edtNrp.setText(nrp);
        edtNama.setText(nama);
        edtEmail.setText(email);
        edtJurusan.setText(jurusan);

        btnUpdate.setOnClickListener(view -> {
            String nrpBaru = edtNrp.getText().toString();
            String namaBaru = edtNama.getText().toString();
            String emailBaru = edtEmail.getText().toString();
            String jurusanBaru = edtJurusan.getText().toString();

            if (TextUtils.isEmpty(nrpBaru) || TextUtils.isEmpty(namaBaru) ||
                    TextUtils.isEmpty(emailBaru) || TextUtils.isEmpty(jurusanBaru)) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            updateMahasiswa(id, nrpBaru, namaBaru, emailBaru, jurusanBaru);
        });
    }

    private void updateMahasiswa(String id, String nrp, String nama, String email, String jurusan) {
        showLoading(true);
        Call<AddMahasiswaResponse> call = ApiConfig.getApiService().updateMahasiswa(id, nrp, nama, email, jurusan);
        call.enqueue(new Callback<AddMahasiswaResponse>() {
            @Override
            public void onResponse(Call<AddMahasiswaResponse> call, Response<AddMahasiswaResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    Toast.makeText(EditMahasiswaActivity.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditMahasiswaActivity.this, "Gagal update data", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AddMahasiswaResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(EditMahasiswaActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}