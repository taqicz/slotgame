package com.example.roomreservation.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "RoomReservationPref";
    private static final String KEY_AUTH_TOKEN = "auth_token";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /**
     * Konstruktor untuk inisialisasi SharedPreferences
     *
     * @param context Context aplikasi
     */
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Simpan token autentikasi pengguna
     *
     * @param token Token autentikasi
     */
    public void saveAuthToken(String token) {
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply(); // Simpan perubahan secara asinkron
    }

    /**
     * Ambil token autentikasi pengguna
     *
     * @return Token autentikasi
     */
    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    /**
     * Periksa apakah pengguna sudah login
     *
     * @return True jika pengguna sudah login, false jika tidak
     */
    public boolean isLoggedIn() {
        return getAuthToken() != null;
    }

    /**
     * Hapus token autentikasi (logout pengguna)
     */
    public void logout() {
        editor.remove(KEY_AUTH_TOKEN);
        editor.apply();
    }
}

