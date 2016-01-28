package com.example.android.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movies.adapter.ReviewCommentAdapter;
import com.example.android.movies.model.MovieDetail;
import com.example.android.movies.model.Review;
import com.example.android.movies.util.ReviewJsonResponse;
import com.example.android.movies.util.TMDbService;
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
    private MovieDetail movieDetail;

    //URL prefix to fetch movie poster from themoviedb.org
    private final String MOVIE_DB_BASE_URL = "http://image.tmdb.org/t/p/w185";

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewCommentAdapter = new ReviewCommentAdapter(getActivity(), new ArrayList<Review>());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (movieDetail != null) {
            new FetchMovieReview().execute(Integer.toString(movieDetail.getId()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        LinearListView reviewListView = (LinearListView) rootView.findViewById(R.id.review_list);
        reviewListView.setAdapter(reviewCommentAdapter);

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

    public class FetchMovieReview extends AsyncTask<String,Void,List<Review>> {

        private final String LOG_TAG = FetchMovieReview.class.getSimpleName();

        @Override
        protected List<Review> doInBackground(String... params) {
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://api.themoviedb.org/3/")
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

            // This will only happen if there was an error getting or parsing the forecast.
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
}
