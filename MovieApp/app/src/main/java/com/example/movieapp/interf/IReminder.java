package com.example.movieapp.interf;

import com.example.movieapp.model.Reminder;

public interface IReminder {
    void insertReminder(Reminder reminder);
    void deleteReminder(Reminder reminder);
}
