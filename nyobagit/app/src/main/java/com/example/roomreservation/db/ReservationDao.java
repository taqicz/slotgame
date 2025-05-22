package com.example.roomreservation.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import com.example.roomreservation.db.models.Reservation;

import java.util.List;

@Dao
public interface ReservationDao {

    // Get all reservations
    @Query("SELECT * FROM reservations")
    List<Reservation> getAllReservations();

    // Get reservation by ID
    @Query("SELECT * FROM reservations WHERE id = :reservationId")
    Reservation getReservationById(int reservationId);

    // Insert a new reservation
    @Insert
    void insertReservation(Reservation reservation);

    // Delete a reservation
    @Delete
    void deleteReservation(Reservation reservation);

    // Delete all reservations
    @Query("DELETE FROM reservations")
    void deleteAllReservations();
}

