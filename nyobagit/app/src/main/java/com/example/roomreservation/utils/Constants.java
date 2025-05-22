package com.example.roomreservation.utils;

/**
 * Kelas Constants berfungsi sebagai tempat penyimpanan berbagai konstanta global
 * yang digunakan di seluruh aplikasi.
 */
public class Constants {

    // Base URL untuk API
    public static final String BASE_URL = "https://api.example.com/";

    // Format tanggal yang digunakan di aplikasi
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    // Format waktu yang digunakan di aplikasi
    public static final String TIME_FORMAT = "HH:mm";

    // Format lengkap untuk tanggal dan waktu
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    // Status ruangan
    public static final String ROOM_STATUS_AVAILABLE = "available";
    public static final String ROOM_STATUS_UNAVAILABLE = "unavailable";

    // Nama-nama tabel untuk database Room
    public static final String TABLE_ROOMS = "rooms";
    public static final String TABLE_RESERVATIONS = "reservations";

    // Kunci untuk bundle Intent
    public static final String INTENT_KEY_ROOM_ID = "room_id";
    public static final String INTENT_KEY_ROOM_NAME = "room_name";
    public static final String INTENT_KEY_DATE = "date";
    public static final String INTENT_KEY_START_TIME = "start_time";
    public static final String INTENT_KEY_END_TIME = "end_time";

    // Pesan kesalahan umum
    public static final String ERROR_NETWORK = "Gagal menghubungi server. Periksa koneksi internet Anda.";
    public static final String ERROR_UNKNOWN = "Terjadi kesalahan yang tidak diketahui. Silakan coba lagi.";
}

