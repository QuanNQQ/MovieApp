package com.example.movieapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminder")
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    private Integer id = 0;

    public double vote_average;

    public String title;

    public String poster_path;

    public String release_date;

    private String date;

    public Reminder(Integer id, double vote_average, String title, String poster_path, String release_date, String date) {
        this.id = id;
        this.vote_average = vote_average;
        this.title = title;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.date = date;
    }

    public Reminder() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

