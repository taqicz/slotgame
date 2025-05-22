package com.example.roomreservation.api;

import com.example.roomreservation.db.models.Reservation;
import com.example.roomreservation.db.models.Room;
import com.example.roomreservation.db.models.User;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

// ApiService Interface
public interface ApiService {

    // Base URL of the API
    String BASE_URL = "https://api.example.com/";

    // Retrofit Instance
    static ApiService create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

    /**
     * Endpoint untuk mendapatkan daftar ruangan
     *
     * @param token Token autentikasi pengguna (Bearer token)
     * @return List of Room
     */
    @GET("rooms")
    Call<List<Room>> getRooms(@Header("Authorization") String token);

    /**
     * Endpoint untuk mendapatkan detail ruangan berdasarkan ID
     *
     * @param token  Token autentikasi pengguna (Bearer token)
     * @param roomId ID ruangan
     * @return Detail ruangan
     */
    @GET("rooms/{id}")
    Call<Room> getRoomDetails(@Header("Authorization") String token, @Path("id") int roomId);

    /**
     * Endpoint untuk membuat reservasi
     *
     * @param token       Token autentikasi pengguna (Bearer token)
     * @param reservation Data reservasi dalam bentuk objek
     * @return JSON response berisi status reservasi
     */
    @POST("reservations")
    Call<JSONObject> createReservation(@Header("Authorization") String token, @Body Reservation reservation);

    /**
     * Endpoint untuk login pengguna
     *
     * @param email    Email pengguna
     * @param password Password pengguna
     * @return JSON response berisi token autentikasi
     */
    @FormUrlEncoded
    @POST("auth/login")
    Call<JSONObject> loginUser(@Field("email") String email, @Field("password") String password);

    @POST("users/register")
    Call<JSONObject> registerUser(@Body User user);
}