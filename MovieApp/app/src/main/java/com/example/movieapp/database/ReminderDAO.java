package com.example.movieapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.movieapp.model.Movie;
import com.example.movieapp.model.Reminder;

import java.util.List;

@Dao
public interface ReminderDAO {

    @Insert
    void insertReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);

    @Query("Select * from reminder")
    List<Reminder> getReminder();

}
