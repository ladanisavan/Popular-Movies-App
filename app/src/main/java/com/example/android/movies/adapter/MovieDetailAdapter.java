package com.example.android.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.movies.R;
import com.example.android.movies.model.MovieDetail;
import com.example.android.movies.util.Constants;
import com.google.common.base.Strings;

import java.util.List;

/**
 * Created by savan on 28/12/2015.
 */
public class MovieDetailAdapter extends ArrayAdapter<MovieDetail> {

    private final String LOG_TAG = MovieDetailAdapter.class.getSimpleName();

    private Context mContext;

    public MovieDetailAdapter(Context context, List<MovieDetail> movies) {
        super(context, 0, movies);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieDetail movieDetail = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        ImageView moviePosterView = (ImageView) convertView.findViewById(R.id.movie_image);
        /*Picasso.with(getContext())
                .load(Constants.MOVIE_DB_IMAGE_BASE_URL + movieDetail.getPosterPath())
                .resize(Constants.MOVIE_POSTER_WIDTH, Constants.MOVIE_POSTER_HEIGHT)
                .centerCrop()
                .into(moviePosterView);*/
        Glide.with(getContext())
                .load(Constants.MOVIE_DB_IMAGE_BASE_URL + movieDetail.getPosterPath())
                .override(Constants.MOVIE_POSTER_WIDTH, Constants.MOVIE_POSTER_HEIGHT)
                .centerCrop()
                .into(moviePosterView);
        TextView movieYear = (TextView) convertView.findViewById(R.id.movie_year);
        if(!Strings.isNullOrEmpty(movieDetail.getReleaseDate())){
            movieYear.setText(movieDetail.getReleaseDate().substring(0,4));
        }
        TextView movieRating = (TextView) convertView.findViewById(R.id.movie_rating);
        //movieRating.setText(String.valueOf(String.format("%.1f", movieDetail.getVoteAvg())).concat("/10"));
        float rating = movieDetail.getVoteAvg();
        movieRating.setText(String.format(mContext.getString(R.string.movie_rating_format), rating));

        return convertView;
    }
}
