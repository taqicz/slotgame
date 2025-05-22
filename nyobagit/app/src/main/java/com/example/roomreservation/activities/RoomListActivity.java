package com.example.roomreservation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomreservation.R;
import com.example.roomreservation.adapters.RoomAdapter;
import com.example.roomreservation.api.ApiClient;
import com.example.roomreservation.api.ApiService;
import com.example.roomreservation.db.models.Room;
import com.example.roomreservation.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomListActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {

    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        // Inisialisasi komponen UI
        recyclerView = findViewById(R.id.rv_room_list);
        progressBar = findViewById(R.id.progress_bar);

        // Inisialisasi Retrofit API Service dan SessionManager
        apiService = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(this);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomAdapter = new RoomAdapter(this);
        recyclerView.setAdapter(roomAdapter);

        // Ambil data ruangan
        fetchRooms();
    }

    /**
     * Fungsi untuk mengambil data ruangan dari server menggunakan API
     */
    private void fetchRooms() {
        // Tampilkan progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Panggil API untuk mendapatkan daftar ruangan
        Call<List<Room>> call = apiService.getRooms("Bearer " + sessionManager.getAuthToken());
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // Tampilkan data ruangan di RecyclerView
                    roomAdapter.setRoomList(response.body());
                } else {
                    Toast.makeText(RoomListActivity.this, "Gagal memuat data ruangan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                // Sembunyikan progress bar
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RoomListActivity.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Callback saat ruangan dipilih dari daftar
     *
     * @param room Ruangan yang dipilih
     */
    @Override
    public void onRoomClick(Room room) {
        // Navigasi ke RoomDetailActivity dengan data ruangan
        Intent intent = new Intent(this, RoomDetailActivity.class);
        intent.putExtra("room_id", room.id);
        startActivity(intent);
    }
}

