package com.example.movieapp.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.movieapp.model.Movie;

import java.util.List;

@Dao
public interface MovieDAO {

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM movie")
    List<Movie> getMovie();

    @Query("SELECT * FROM movie WHERE id == (:id)")
    Movie getMovieById(int id);

    @Query("SELECT COUNT(*) FROM movie")
    int countMovie();

}
