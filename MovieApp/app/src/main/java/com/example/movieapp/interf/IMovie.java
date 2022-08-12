package com.example.movieapp.interf;

import com.example.movieapp.model.Movie;

public interface IMovie {
        void onClickItem(Movie movie);
        void insertMovie(Movie movie);
        void getMovie(Movie movie);
        void delete(Movie movie);
}
