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
import com.example.roomreservation.db.models.Room;
import com.example.roomreservation.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomDetailActivity extends AppCompatActivity {

    private TextView txtRoomName, txtLocation, txtCapacity, txtPrice, txtStatus;
    private Button btnReserve;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SessionManager sessionManager;

    private int roomId; // ID ruangan yang dipilih

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        // Inisialisasi komponen UI
        txtRoomName = findViewById(R.id.tv_room_name);
        txtLocation = findViewById(R.id.tv_location);
        txtCapacity = findViewById(R.id.tv_capacity);
        txtPrice = findViewById(R.id.tv_price);
        txtStatus = findViewById(R.id.tv_status);
        btnReserve = findViewById(R.id.btn_reserve);
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

        // Ambil detail ruangan
        fetchRoomDetails();

        // Tombol untuk melakukan reservasi
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToReservation();
            }
        });
    }

    /**
     * Fungsi untuk mengambil detail ruangan dari server
     */
    private void fetchRoomDetails() {
        // Tampilkan progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Panggil API untuk mendapatkan detail ruangan
        Call<Room> call = apiService.getRoomDetails("Bearer " + sessionManager.getAuthToken(), roomId);
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // Tampilkan data ruangan di UI
                    Room room = response.body();
                    txtRoomName.setText(room.nama);
                    txtLocation.setText("Lokasi: " + room.lokasi);
                    txtCapacity.setText("Kapasitas: " + room.kapasitas + " orang");
                    txtPrice.setText("Harga: Rp " + room.harga);
                    txtStatus.setText("Status: " + (room.status.equals("available") ? "Tersedia" : "Tidak Tersedia"));

                    // Nonaktifkan tombol reservasi jika ruangan tidak tersedia
                    btnReserve.setEnabled(room.status.equals("available"));
                } else {
                    Toast.makeText(RoomDetailActivity.this, "Gagal memuat detail ruangan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RoomDetailActivity.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fungsi untuk navigasi ke halaman reservasi ruangan
     */
    private void navigateToReservation() {
        Intent intent = new Intent(RoomDetailActivity.this, ReservationActivity.class);
        intent.putExtra("room_id", roomId);
        startActivity(intent);
    }
}

