package com.example.android.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movies.model.MovieDetail;
import com.squareup.picasso.Picasso;

/**
 * A fragment to view selected movie details
 */
public class MovieDetailActivityFragment extends Fragment {

    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();

    private MovieDetail movieDetail;

    //URL prefix to fetch movie poster from themoviedb.org
    private final String MOVIE_DB_BASE_URL = "http://image.tmdb.org/t/p/w185";

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();
        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)){
            movieDetail = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            ((TextView)rootView.findViewById(R.id.movie_title)).setText(movieDetail.getTitle());
            Picasso.with(getActivity()).load(MOVIE_DB_BASE_URL + movieDetail.getPosterPath())
                    .into((ImageView) rootView.findViewById(R.id.movie_thumbnail));
            ((TextView) rootView.findViewById(R.id.movie_release_date)).setText(movieDetail.getReleaseDate());
            ((TextView)rootView.findViewById(R.id.movie_vote_avg))
                    .setText(String.valueOf(String.format("%.1f", movieDetail.getVoteAvg())).concat("/10"));
            ((TextView)rootView.findViewById(R.id.movie_overview))
                    .setText(movieDetail.getMovieOverview());
        }
        return rootView;
    }
}
