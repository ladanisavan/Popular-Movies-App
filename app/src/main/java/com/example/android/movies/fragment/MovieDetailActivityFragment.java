package com.example.android.movies.fragment;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movies.BuildConfig;
import com.example.android.movies.R;
import com.example.android.movies.adapter.ReviewCommentAdapter;
import com.example.android.movies.adapter.TrailerAdapter;
import com.example.android.movies.data.MovieContract;
import com.example.android.movies.model.MovieDetail;
import com.example.android.movies.model.Review;
import com.example.android.movies.model.Trailer;
import com.example.android.movies.rest.ReviewJsonResponse;
import com.example.android.movies.rest.TMDbService;
import com.example.android.movies.rest.TrailerJsonResponse;
import com.example.android.movies.util.Constants;
import com.example.android.movies.util.Utils;
import com.google.common.base.Strings;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * A fragment to view selected movie details
 */
public class MovieDetailActivityFragment extends Fragment {

    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();

    private ArrayAdapter<Review> reviewCommentAdapter;
    private ArrayAdapter<Trailer> trailerArrayAdapter;
    private MovieDetail movieDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewCommentAdapter = new ReviewCommentAdapter(getActivity(), new ArrayList<Review>());
        trailerArrayAdapter = new TrailerAdapter(getActivity(), new ArrayList<Trailer>());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (movieDetail != null) {
            new FetchMovieReview().execute(Integer.toString(movieDetail.getId()));
            new FetchMovieTrailer().execute(Integer.toString(movieDetail.getId()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Bundle arguments = getArguments();
        if(arguments != null){
            movieDetail = getArguments().getParcelable(Constants.MOVIE_DETAIL_KEY);
        }
        LinearListView trailerList = (LinearListView) rootView.findViewById(R.id.trailer_list_layout);
        trailerList.setEmptyView(rootView.findViewById(R.id.no_trailer_view));
        trailerList.setAdapter(trailerArrayAdapter);

        LinearListView reviewList = (LinearListView) rootView.findViewById(R.id.review_list);
        reviewList.setEmptyView(rootView.findViewById(R.id.no_review_view));
        reviewList.setAdapter(reviewCommentAdapter);

        ((TextView) rootView.findViewById(R.id.movie_title)).setText(movieDetail.getTitle());
        Picasso.with(getActivity()).load(Constants.MOVIE_DB_IMAGE_BASE_URL + movieDetail.getPosterPath())
                .resize(Constants.MOVIE_POSTER_WIDTH, Constants.MOVIE_POSTER_HEIGHT)
                .centerCrop()
                .into((ImageView) rootView.findViewById(R.id.movie_thumbnail));
        if(!Strings.isNullOrEmpty(movieDetail.getReleaseDate())){
            ((TextView) rootView.findViewById(R.id.movie_release_date)).setText(movieDetail.getReleaseDate().substring(0,4));
        }
        ((TextView) rootView.findViewById(R.id.movie_vote_avg)).setText(String.valueOf(String.format(getContext().getString(R.string.movie_rating_format), movieDetail.getVoteAvg())));
        ((TextView) rootView.findViewById(R.id.movie_overview)).setText(movieDetail.getMovieOverview());

        final Button button = (Button) rootView.findViewById(R.id.favorite_btn);
        if(Utils.isFavorited(getActivity(),movieDetail.getId())>0){
            button.setText(Constants.BTN_LB_UNFAVORITE);
        }else{
            button.setText(Constants.BTN_LB_MARK_AS_FAVORITE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().equals(Constants.BTN_LB_MARK_AS_FAVORITE)) {
                    button.setText(Constants.BTN_LB_UNFAVORITE);
                    addMovieToFavoriteList();
                } else {
                    button.setText(Constants.BTN_LB_MARK_AS_FAVORITE);
                    removeFromFavorite();
                }
            }
        });

        return rootView;
    }

    public class FetchMovieReview extends AsyncTask<String,Void,List<Review>> {

        private final String LOG_TAG = FetchMovieReview.class.getSimpleName();

        @Override
        protected List<Review> doInBackground(String... params) {
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.THE_MOVIE_DB_BASE_URI)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                TMDbService tmDbService = retrofit.create(TMDbService.class);
                Call<ReviewJsonResponse> call = tmDbService.getReviewList(params[0], BuildConfig.MOVIE_DB_API_KEY);
                ReviewJsonResponse jsonResponse = call.execute().body();
                Log.v(LOG_TAG, jsonResponse.getReviewList().toString());
                return jsonResponse.getReviewList();
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Review> reviewList) {
            if (reviewList != null && reviewList.size() > 0) {
                reviewCommentAdapter.clear();
                for(Review review : reviewList) {
                    reviewCommentAdapter.add(review);
                }
            }
        }
    }

    public class FetchMovieTrailer extends AsyncTask<String,Void,List<Trailer>> {

        private final String LOG_TAG = FetchMovieTrailer.class.getSimpleName();

        @Override
        protected List<Trailer> doInBackground(String... params) {
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.THE_MOVIE_DB_BASE_URI)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                TMDbService tmDbService = retrofit.create(TMDbService.class);
                Call<TrailerJsonResponse> call = tmDbService.getTrailerList(params[0], BuildConfig.MOVIE_DB_API_KEY);
                TrailerJsonResponse jsonResponse = call.execute().body();
                Log.v(LOG_TAG, jsonResponse.getTrailerList().toString());
                return jsonResponse.getTrailerList();
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailerList) {
            if (trailerList != null && trailerList.size() > 0) {
                trailerArrayAdapter.clear();
                for(Trailer trailer : trailerList) {
                    trailerArrayAdapter.add(trailer);
                }
            }
        }
    }

    private void addMovieToFavoriteList(){
        ContentValues values = new ContentValues();

        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieDetail.getId());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movieDetail.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_IMAGE, movieDetail.getPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_IMAGE2, "");
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieDetail.getMovieOverview());
        values.put(MovieContract.MovieEntry.COLUMN_RATING, movieDetail.getVoteAvg());
        values.put(MovieContract.MovieEntry.COLUMN_DATE, movieDetail.getReleaseDate());

        getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

        Toast mToast = Toast.makeText(getActivity(), getContext().getString(R.string.added_to_favorites), Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void removeFromFavorite(){
        getActivity().getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{Integer.toString(movieDetail.getId())});

        Toast mToast = Toast.makeText(getActivity(), getContext().getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT);
        mToast.show();
    }
}
