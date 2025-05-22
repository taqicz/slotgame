package com.example.roomreservation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomreservation.R;
import com.example.roomreservation.activities.LoginActivity;
import com.example.roomreservation.activities.RoomListActivity;
import com.example.roomreservation.utils.SessionManager;

/**
 * MainActivity adalah activity pertama yang dijalankan saat aplikasi dibuka.
 * Activity ini bertanggung jawab untuk memeriksa status login pengguna dan
 * mengarahkan mereka ke halaman yang sesuai.
 */
public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi SessionManager
        sessionManager = new SessionManager(this);

        // Simulasi splash screen menggunakan Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginStatus();
            }
        }, 2000); // Waktu tunggu 2 detik
    }

    /**
     * Memeriksa status login pengguna dan mengarahkan ke halaman yang sesuai.
     */
    private void checkLoginStatus() {
        if (sessionManager.isLoggedIn()) {
            // Pengguna sudah login, arahkan ke RoomListActivity
            navigateToRoomList();
        } else {
            // Pengguna belum login, arahkan ke LoginActivity
            navigateToLogin();
        }
    }

    /**
     * Mengarahkan pengguna ke RoomListActivity.
     */
    private void navigateToRoomList() {
        Intent intent = new Intent(MainActivity.this, RoomListActivity.class);
        startActivity(intent);
        finish(); // Tutup MainActivity agar tidak bisa kembali ke splash screen
    }

    /**
     * Mengarahkan pengguna ke LoginActivity.
     */
    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Tutup MainActivity agar tidak bisa kembali ke splash screen
    }
}

