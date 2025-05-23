package com.example.javaminggu10_1;

import com.example.javaminggu10_1.model.AddMahasiswaResponse;
import com.example.javaminggu10_1.model.Mahasiswa;
import com.example.javaminggu10_1.model.MahasiswaResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Mendapatkan data mahasiswa berdasarkan NRP
    @GET("mahasiswa")
    Call<MahasiswaResponse> getMahasiswa(@Query("nrp") String nrp);

    // Menambah mahasiswa baru
    @POST("mahasiswa")
    @FormUrlEncoded
    Call<AddMahasiswaResponse> addMahasiswa(
            @Field("nrp") String nrp,
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("jurusan") String jurusan
    );

    @FormUrlEncoded
    @POST("mahasiswa/update/{id}")
    Call<AddMahasiswaResponse> updateMahasiswa(
            @Path("id") String id,
            @Field("nrp") String nrp,
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("jurusan") String jurusan
    );
    // Mendapatkan seluruh mahasiswa

    @GET("mahasiswa")
    Call<MahasiswaResponse> getAllMahasiswa();
    // Update data mahasiswa (gunakan endpoint sesuai backend Anda!)

    // Hapus mahasiswa (gunakan endpoint sesuai backend Anda!)
    @DELETE("mahasiswa/{id}")
    Call<AddMahasiswaResponse> deleteMahasiswa(
            @Path("id") String id
    );
}