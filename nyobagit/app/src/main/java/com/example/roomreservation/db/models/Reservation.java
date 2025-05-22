package com.example.roomreservation.db.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Model data untuk entitas Reservation, yang direpresentasikan sebagai tabel dalam database.
 */
@Entity(
        tableName = "reservations",
        foreignKeys = @ForeignKey(
                entity = Room.class,
                parentColumns = "id",
                childColumns = "roomId",
                onDelete = CASCADE
        )
)
public class Reservation {

    @PrimaryKey(autoGenerate = true)
    public int id; // ID unik untuk setiap reservasi

    public int roomId; // ID ruangan yang direferensikan
    public String date; // Tanggal reservasi (format: YYYY-MM-DD)
    public String startTime; // Waktu mulai (format: HH:mm)
    public String endTime; // Waktu selesai (format: HH:mm)

    /**
     * Constructor default untuk Reservation.
     */
    public Reservation() {
    }

    /**
     * Constructor untuk membuat instance Reservation dengan parameter lengkap.
     *
     * @param id        ID reservasi.
     * @param roomId    ID ruangan yang direferensikan.
     * @param date      Tanggal reservasi.
     * @param startTime Waktu mulai reservasi.
     * @param endTime   Waktu selesai reservasi.
     */
    public Reservation(int id, int roomId, String date, String startTime, String endTime) {
        this.id = id;
        this.roomId = roomId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getter dan Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}

