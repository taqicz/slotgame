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
import com.example.roomreservation.db.models.Reservation;
import com.example.roomreservation.utils.SessionManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity {

    private EditText editDate, editStartTime, editEndTime;
    private Button btnSubmitReservation;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SessionManager sessionManager;

    private int roomId; // ID ruangan yang akan dipesan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // Inisialisasi komponen UI
        editDate = findViewById(R.id.edit_date);
        editStartTime = findViewById(R.id.edit_start_time);
        editEndTime = findViewById(R.id.edit_end_time);
        btnSubmitReservation = findViewById(R.id.btn_submit_reservation);
        progressBar = findViewById(R.id.progress_bar);

        // Inisialisasi SessionManager dan ApiService
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        // Ambil room ID dari intent
        roomId = getIntent().getIntExtra("room_id", -1);

        if (roomId == -1) {
            Toast.makeText(this, "Data ruangan tidak tersedia", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Tombol untuk mengirim reservasi
        btnSubmitReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReservation();
            }
        });
    }

    /**
     * Fungsi untuk mengirim data reservasi ke server
     */
    private void submitReservation() {
        // Ambil input dari pengguna
        String date = editDate.getText().toString().trim();
        String startTime = editStartTime.getText().toString().trim();
        String endTime = editEndTime.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(date)) {
            editDate.setError("Tanggal tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(startTime)) {
            editStartTime.setError("Waktu mulai tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(endTime)) {
            editEndTime.setError("Waktu selesai tidak boleh kosong");
            return;
        }

        // Tampilkan progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Buat objek reservasi
        Reservation reservation = new Reservation();
        reservation.setRoomId(roomId);
        reservation.setDate(date);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);

        // Panggil API untuk mengirim data reservasi
        Call<JSONObject> call = apiService.createReservation(
                "Bearer " + sessionManager.getAuthToken(),
                reservation
        );
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(ReservationActivity.this, "Reservasi berhasil dibuat", Toast.LENGTH_SHORT).show();
                    finish(); // Tutup activity setelah sukses
                } else {
                    Toast.makeText(ReservationActivity.this, "Gagal membuat reservasi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ReservationActivity.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

