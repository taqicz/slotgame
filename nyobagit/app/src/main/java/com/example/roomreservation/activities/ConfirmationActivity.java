package com.example.roomreservation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomreservation.R;
import com.example.roomreservation.api.ApiClient;
import com.example.roomreservation.api.ApiService;
import com.example.roomreservation.db.models.Reservation;
import com.example.roomreservation.utils.SessionManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity {

    private TextView txtRoomName, txtDate, txtStartTime, txtEndTime;
    private Button btnConfirm, btnCancel;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SessionManager sessionManager;

    private int roomId;
    private String roomName, date, startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Inisialisasi komponen UI
        txtRoomName = findViewById(R.id.tv_confirm_room_name);
        txtDate = findViewById(R.id.tv_confirm_date);
        txtStartTime = findViewById(R.id.tv_confirm_start_time);
        txtEndTime = findViewById(R.id.tv_confirm_end_time);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);
        progressBar = findViewById(R.id.progress_bar);

        // Inisialisasi SessionManager dan ApiService
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        // Ambil data reservasi dari intent
        roomId = getIntent().getIntExtra("room_id", -1);
        roomName = getIntent().getStringExtra("room_name");
        date = getIntent().getStringExtra("date");
        startTime = getIntent().getStringExtra("start_time");
        endTime = getIntent().getStringExtra("end_time");

        if (roomId == -1 || roomName == null || date == null || startTime == null || endTime == null) {
            Toast.makeText(this, "Data reservasi tidak lengkap", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Tampilkan data reservasi di UI
        txtRoomName.setText("Nama Ruangan: " + roomName);
        txtDate.setText("Tanggal: " + date);
        txtStartTime.setText("Waktu Mulai: " + startTime);
        txtEndTime.setText("Waktu Selesai: " + endTime);

        // Tombol Konfirmasi
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmReservation();
            }
        });

        // Tombol Batal
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Tutup aktivitas dan kembali ke halaman sebelumnya
            }
        });
    }

    /**
     * Fungsi untuk mengirim konfirmasi reservasi ke server
     */
    private void confirmReservation() {
        // Tampilkan progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Buat objek reservasi
        Reservation reservation = new Reservation();
        reservation.setRoomId(roomId);
        reservation.setDate(date);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);

        // Panggil API untuk mengonfirmasi reservasi
        Call<JSONObject> call = apiService.createReservation(
                "Bearer " + sessionManager.getAuthToken(), reservation
        );
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(ConfirmationActivity.this, "Reservasi berhasil dikonfirmasi", Toast.LENGTH_SHORT).show();
                    finish(); // Tutup aktivitas setelah sukses
                } else {
                    Toast.makeText(ConfirmationActivity.this, "Gagal mengonfirmasi reservasi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ConfirmationActivity.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

