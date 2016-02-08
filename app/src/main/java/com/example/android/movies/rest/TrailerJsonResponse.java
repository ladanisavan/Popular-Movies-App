package com.example.android.movies.rest;

import com.example.android.movies.model.Trailer;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by savan on 03/02/2016.
 */
public class TrailerJsonResponse {
    @SerializedName("page")
    private int id;
    @SerializedName("results")

    private List<Trailer> trailerList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
    }

    @Override
    public String toString() {
        return "TrailerJsonResponse{" +
                "id=" + id +
                ", trailerList=" + trailerList +
                '}';
    }
}
