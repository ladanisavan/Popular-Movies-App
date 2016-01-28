package com.example.android.movies.util;

import com.example.android.movies.model.MovieDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by savan on 15/01/2016.
 */
public class MovieJsonResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<MovieDetail> movieList;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public List<MovieDetail> getMovieList() {
        return movieList;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setMovieList(List<MovieDetail> movieList) {
        this.movieList = movieList;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "MovieJsonResponse{" +
                "page=" + page +
                ", movieList=" + movieList +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                '}';
    }
}
