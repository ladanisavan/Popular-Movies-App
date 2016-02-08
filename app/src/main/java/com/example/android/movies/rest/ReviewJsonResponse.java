package com.example.android.movies.rest;

import com.example.android.movies.model.Review;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by savan on 18/01/2016.
 */
public class ReviewJsonResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Review> reviewList;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "ReviewJsonResponse{" +
                "page=" + page +
                ", reviewList=" + reviewList +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                '}';
    }
}
