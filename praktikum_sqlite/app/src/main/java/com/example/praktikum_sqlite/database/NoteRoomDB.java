package com.example.praktikum_sqlite.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteRoomDB extends RoomDatabase {
    public abstract DAONote daoNote();
    private static volatile NoteRoomDB INSTANCE;
    public static NoteRoomDB getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (NoteRoomDB.class){
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(),
                                NoteRoomDB.class, "note_db").build();
            }
        }
        return INSTANCE;
    }
}