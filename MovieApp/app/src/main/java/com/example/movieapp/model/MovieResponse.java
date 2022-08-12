package com.example.movieapp.model;

public class MovieResponse {
private Movie movie;

    public MovieResponse(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
