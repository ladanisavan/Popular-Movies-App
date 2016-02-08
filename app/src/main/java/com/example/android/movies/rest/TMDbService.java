package com.example.android.movies.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by savan on 15/01/2016.
 */
public interface TMDbService {

    @GET("discover/movie")
    Call<MovieJsonResponse> getMovieList(@Query("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewJsonResponse> getReviewList(@Path("id") String movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailerJsonResponse> getTrailerList(@Path("id") String movieId, @Query("api_key") String apiKey);
}
