package com.example.javaminggu10_1.model;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.javaminggu10_1.ApiConfig;
import com.example.javaminggu10_1.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMahasiswaActivity extends
        AppCompatActivity {

    private EditText edtChecNrp;
    private Button btnSearch;
    private ProgressBar progressBar;
    private TextView tvNrp;
    private TextView tvNama;
    private TextView tvEmail;
    private TextView tvJurusan;
    private List<Mahasiswa> mahasiswaList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_mahasiswa);

        edtChecNrp = findViewById(R.id.edtChckNrp);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = findViewById(R.id.progressBar);
        tvNrp = findViewById(R.id.tvValNrp);
        tvNama = findViewById(R.id.tvValNama);
        tvEmail = findViewById(R.id.tvValEmail);
        tvJurusan = findViewById(R.id.tvValJurusan);

        mahasiswaList = new ArrayList<>();


        btnSearch.setOnClickListener(view -> {
            showLoading(true);
            String nrp = edtChecNrp.getText().toString();
            if (nrp.isEmpty()){
                edtChecNrp.setError("Silahakan Isi nrp terlebih dahulu");
                        showLoading(false);
            }else{
                Call<MahasiswaResponse> client =
                        ApiConfig.getApiService().getMahasiswa(nrp);
                client.enqueue(new
                                       Callback<MahasiswaResponse>() {
                                           @Override
                                           public void
                                           onResponse(Call<MahasiswaResponse> call,
                                                      Response<MahasiswaResponse> response) {
                                               if (response.isSuccessful()){
                                                   if (response.body() != null){
                                                       showLoading(false);
                                                       mahasiswaList =
                                                               response.body().getData();
                                                       setData(mahasiswaList);
                                                   }
                                               } else {
                                                   if (response.body() != null)
                                                   {
                                                       Log.e("", "onFailure: " +
                                                               response.message());
                                                   }
                                               }
                                           }

                                           @Override
                                           public void
                                           onFailure(Call<MahasiswaResponse> call, Throwable t) {
                                               showLoading(false);
                                               Log.e("Error Retrofit",
                                                       "onFailure: " + t.getMessage());
                                           }
                                       });
            }

        });
    }

    private void setData(List<Mahasiswa> mahasiswaList) {
        tvNrp.setText(mahasiswaList.get(0).getNrp());
        tvNama.setText(mahasiswaList.get(0).getNama());
        tvEmail.setText(mahasiswaList.get(0).getEmail());

        tvJurusan.setText(mahasiswaList.get(0).getJurusan());
    }


    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
