package com.example.roomreservation.db.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Model data untuk entitas Room, yang direpresentasikan sebagai tabel dalam database.
 */
@Entity(tableName = "rooms")
public class Room {

    @PrimaryKey
    public int id; // ID unik untuk setiap ruangan

    public String nama; // Nama ruangan
    public int kapasitas; // Kapasitas maksimum ruangan (jumlah orang)
    public String lokasi; // Lokasi ruangan
    public double harga; // Harga sewa ruangan
    public String status; // Status ruangan, misalnya "available" atau "unavailable"

    /**
     * Constructor default untuk Room.
     */
    public Room() {
    }

    /**
     * Constructor untuk membuat instance Room dengan parameter lengkap.
     *
     * @param id        ID ruangan.
     * @param nama      Nama ruangan.
     * @param kapasitas Kapasitas ruangan.
     * @param lokasi    Lokasi ruangan.
     * @param harga     Harga sewa ruangan.
     * @param status    Status ruangan.
     */
    public Room(int id, String nama, int kapasitas, String lokasi, double harga, String status) {
        this.id = id;
        this.nama = nama;
        this.kapasitas = kapasitas;
        this.lokasi = lokasi;
        this.harga = harga;
        this.status = status;
    }

    // Getter dan Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", kapasitas=" + kapasitas +
                ", lokasi='" + lokasi + '\'' +
                ", harga=" + harga +
                ", status='" + status + '\'' +
                '}';
    }
}

