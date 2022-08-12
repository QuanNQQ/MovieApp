package com.example.movieapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.movieapp.model.Movie;
import com.example.movieapp.model.Reminder;

@Database(entities = {Movie.class, Reminder.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "mvdb.db";
    private static MovieDatabase INSTANCE = null;
    public static synchronized MovieDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public abstract MovieDAO movieDAO();
    public abstract ReminderDAO reminderDAO();

}
