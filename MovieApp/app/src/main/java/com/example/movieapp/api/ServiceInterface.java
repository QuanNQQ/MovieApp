package com.example.movieapp.api;

import com.example.movieapp.model.CastResponse;
import com.example.movieapp.model.MovieResponse;
import com.example.movieapp.model.PopularMovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceInterface {
    @GET("movie/popular")
    Call<PopularMovieResponse> getPopularMovie(@Query("api_key") String api_key, @Query("page") int page);

    @GET("movie/now_playing")
    Call<PopularMovieResponse> getNowPlayingMovie(@Query("api_key") String api_key, @Query("page") int page);

    @GET("movie/top_rated")
    Call<PopularMovieResponse> getTopRateMovie(@Query("api_key") String api_key, @Query("page") int page);

    @GET("movie/upcoming")
    Call<PopularMovieResponse> getUpcomingMovie(@Query("api_key") String api_key, @Query("page") int page);



    @GET("movie/{movieId}")
    Call<MovieResponse> getMovieDetail(@Path("movieId") int id ,@Query("api_key") String api_key );

    @GET("movie/{movieId}/credits")
    Call<CastResponse> getCast(@Path("movieId") int id, @Query("api_key") String api_key);
}
