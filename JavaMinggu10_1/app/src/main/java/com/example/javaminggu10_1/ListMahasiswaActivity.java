package com.example.javaminggu10_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaminggu10_1.adapter.MahasiswaAdapter;
import com.example.javaminggu10_1.model.Mahasiswa;
import com.example.javaminggu10_1.model.MahasiswaResponse;
import com.example.javaminggu10_1.model.AddMahasiswaResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListMahasiswaActivity extends AppCompatActivity {
    private RecyclerView rvMahasiswa;
    private ProgressBar progressBarList;
    private MahasiswaAdapter mahasiswaAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mahasiswa);

        rvMahasiswa = findViewById(R.id.rvMahasiswa);
        progressBarList = findViewById(R.id.progressBarList);

        mahasiswaAdapter = new MahasiswaAdapter(new ArrayList<>(), new MahasiswaAdapter.OnItemActionListener() {
            @Override
            public void onEdit(Mahasiswa mahasiswa) {
                Intent intent = new Intent(ListMahasiswaActivity.this, EditMahasiswaActivity.class);
                intent.putExtra("id", mahasiswa.getId());
                intent.putExtra("nrp", mahasiswa.getNrp());
                intent.putExtra("nama", mahasiswa.getNama());
                intent.putExtra("email", mahasiswa.getEmail());
                intent.putExtra("jurusan", mahasiswa.getJurusan());
                startActivity(intent);
            }

            @Override
            public void onDelete(Mahasiswa mahasiswa, int position) {
                deleteMahasiswa(mahasiswa.getId(), position);
            }
        });

        rvMahasiswa.setLayoutManager(new LinearLayoutManager(this));
        rvMahasiswa.setAdapter(mahasiswaAdapter);

        getAllMahasiswa();
    }

    private void getAllMahasiswa() {
        showLoading(true);
        Call<MahasiswaResponse> call = ApiConfig.getApiService().getAllMahasiswa();
        call.enqueue(new Callback<MahasiswaResponse>() {
            @Override
            public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    mahasiswaAdapter.setData(response.body().getData());
                } else {
                    Toast.makeText(ListMahasiswaActivity.this, "Gagal mendapatkan data!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(ListMahasiswaActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteMahasiswa(String id, int position) {
        Call<AddMahasiswaResponse> call = ApiConfig.getApiService().deleteMahasiswa(id);
        call.enqueue(new Callback<AddMahasiswaResponse>() {
            @Override
            public void onResponse(Call<AddMahasiswaResponse> call, Response<AddMahasiswaResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    mahasiswaAdapter.removeItem(position);
                    Toast.makeText(ListMahasiswaActivity.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListMahasiswaActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AddMahasiswaResponse> call, Throwable t) {
                Toast.makeText(ListMahasiswaActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBarList.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllMahasiswa(); // refresh data setiap kembali ke activity ini
    }
}