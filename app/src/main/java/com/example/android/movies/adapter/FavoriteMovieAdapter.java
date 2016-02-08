package com.example.android.movies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movies.R;
import com.example.android.movies.fragment.FavoriteMovieGridFragment;
import com.example.android.movies.util.Constants;
import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;

/**
 * Created by savan on 07/02/2016.
 */
public class FavoriteMovieAdapter extends CursorAdapter {

    //URL prefix to fetch movie poster from themoviedb.org
    private Context mContext;

    public FavoriteMovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView moviePosterView = (ImageView) view.findViewById(R.id.movie_image);
        Picasso.with(context)
                .load(Constants.MOVIE_DB_IMAGE_BASE_URL + cursor.getString(FavoriteMovieGridFragment.COL_IMAGE))
                .resize(Constants.MOVIE_POSTER_WIDTH, Constants.MOVIE_POSTER_HEIGHT)
                .centerCrop()
                .into(moviePosterView);
        TextView movieYear = (TextView) view.findViewById(R.id.movie_year);
        if(!Strings.isNullOrEmpty(cursor.getString(FavoriteMovieGridFragment.COL_DATE))){
            movieYear.setText(cursor.getString(FavoriteMovieGridFragment.COL_DATE).substring(0,4));
        }
        TextView movieRating = (TextView) view.findViewById(R.id.movie_rating);
        float rating = cursor.getFloat(FavoriteMovieGridFragment.COL_RATING);
        movieRating.setText(String.format(mContext.getString(R.string.movie_rating_format),rating));
    }
}
