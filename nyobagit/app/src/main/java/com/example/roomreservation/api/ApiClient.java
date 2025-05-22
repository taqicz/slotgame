package com.example.roomreservation.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ApiClient adalah class yang bertanggung jawab untuk menyediakan instance Retrofit,
 * yang digunakan untuk membuat panggilan HTTP ke server API.
 */
public class ApiClient {

    // Base URL API - ganti dengan URL server API Anda
    private static final String BASE_URL = "https://api.example.com/";

    // Instance Retrofit
    private static Retrofit retrofit;

    /**
     * Mendapatkan instance Retrofit untuk berkomunikasi dengan server API
     *
     * @return Retrofit instance
     */
    public static Retrofit getClient() {
        // Periksa apakah instance Retrofit sudah dibuat
        if (retrofit == null) {
            // Buat instance baru jika belum ada
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Set URL dasar API
                    .addConverterFactory(GsonConverterFactory.create()) // Konverter JSON ke Java
                    .build();
        }
        return retrofit;
    }
}


