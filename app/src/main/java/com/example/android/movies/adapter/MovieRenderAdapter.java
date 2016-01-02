package com.example.android.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.movies.R;
import com.example.android.movies.model.MovieDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by savan on 28/12/2015.
 */
public class MovieRenderAdapter extends ArrayAdapter<MovieDetail> {

    private final String LOG_TAG = MovieRenderAdapter.class.getSimpleName();

    //URL prefix to fetch movie poster from themoviedb.org
    private final String MOVIE_DB_BASE_URL = "http://image.tmdb.org/t/p/w185";

    public MovieRenderAdapter(Context context, List<MovieDetail> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the MovieDetail object from the ArrayAdapter at the appropriate position
        MovieDetail movieDetail = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        ImageView moviePosterView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load(MOVIE_DB_BASE_URL + movieDetail.getPosterPath()).into(moviePosterView);

        return convertView;
    }
}
