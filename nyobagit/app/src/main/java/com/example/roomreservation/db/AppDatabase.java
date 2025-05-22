package com.example.roomreservation.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.roomreservation.db.ReservationDao;
import com.example.roomreservation.db.models.Reservation;

/**
 * AppDatabase adalah class yang bertanggung jawab untuk menyediakan akses ke database Room.
 */
@Database(entities = {Room.class, Reservation.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // DAO untuk Room dan Reservation
    public abstract RoomDao roomDao();
    public abstract ReservationDao reservationDao();

    /**
     * Mendapatkan instance singleton dari Room Database.
     *
     * @param context Context aplikasi.
     * @return Instance AppDatabase.
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "room_reservation_db")
                    .fallbackToDestructiveMigration() // Menghapus data jika skema berubah
                    .build();
        }
        return instance;
    }
}

