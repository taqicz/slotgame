package com.example.roomreservation.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.roomreservation.db.models.Room;

import java.util.List;

@Dao
public interface RoomDao {

    // Get all rooms
    @Query("SELECT * FROM rooms")
    List<Room> getAllRooms();

    // Get room by ID
    @Query("SELECT * FROM rooms WHERE id = :roomId")
    Room getRoomById(int roomId);

    // Insert a new room
    @Insert
    void insertRoom(Room room);

    // Insert multiple rooms
    @Insert
    void insertRooms(List<Room> rooms);

    // Update room details
    @Update
    void updateRoom(Room room);

    // Delete a room
    @Delete
    void deleteRoom(Room room);

    // Delete all rooms
    @Query("DELETE FROM rooms")
    void deleteAllRooms();
}

