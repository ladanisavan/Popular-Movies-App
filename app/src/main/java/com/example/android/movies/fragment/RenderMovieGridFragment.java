package com.example.android.movies.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.android.movies.BuildConfig;
import com.example.android.movies.R;
import com.example.android.movies.adapter.MovieDetailAdapter;
import com.example.android.movies.model.MovieDetail;
import com.example.android.movies.rest.MovieJsonResponse;
import com.example.android.movies.rest.TMDbService;
import com.example.android.movies.util.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by savan on 05/02/2016.
 */
public class RenderMovieGridFragment extends Fragment {

    private final String LOG_TAG = RenderMovieGridFragment.class.getSimpleName();

    private ArrayAdapter<MovieDetail> movieDetailsAdapter;
    ArrayList<MovieDetail> movieDetailList = new ArrayList<MovieDetail>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey(Constants.MOVIE_DETAIL_LIST)) {
            fetchMovies();
        }
        else {
            movieDetailList = savedInstanceState.getParcelableArrayList(Constants.MOVIE_DETAIL_LIST);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.MOVIE_DETAIL_LIST, movieDetailList);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.tab_fragment, container, false);

        if(movieDetailList != null && movieDetailList.size() > 0){
            movieDetailsAdapter = new MovieDetailAdapter(getActivity(), movieDetailList);
        }else {
            movieDetailsAdapter = new MovieDetailAdapter(getActivity(), new ArrayList<MovieDetail>());
        }

        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(movieDetailsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDetail movieDetail = movieDetailsAdapter.getItem(position);
                ((Callback)getActivity()).onItemSelected(movieDetail);
            }
        });

        return rootView;
    }

    public class FetchMovieDetail extends AsyncTask<String,Void,List<MovieDetail>> {

        private final String LOG_TAG = FetchMovieDetail.class.getSimpleName();

        @Override
        protected List<MovieDetail> doInBackground(String... params) {
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.THE_MOVIE_DB_BASE_URI)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                TMDbService tmDbService = retrofit.create(TMDbService.class);
                Call<MovieJsonResponse> call = tmDbService.getMovieList(params[0], BuildConfig.MOVIE_DB_API_KEY);
                MovieJsonResponse jsonResponse = call.execute().body();
                return jsonResponse.getMovieList();
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieDetail> moviesList) {
            if (moviesList != null && moviesList.size() > 0) {
                movieDetailsAdapter.clear();
                for(MovieDetail movieDetail : moviesList) {
                    movieDetailsAdapter.add(movieDetail);
                }
            }
            movieDetailList.clear();
            movieDetailList.addAll(moviesList);
        }
    }

    private void fetchMovies() {
        FetchMovieDetail fetchMovieDetail = new FetchMovieDetail();
        String sortBy = getArguments().getString(Constants.SORT_BY_KEY);
        fetchMovieDetail.execute(sortBy);
    }

    public interface Callback{
        public void onItemSelected(MovieDetail movieDetail);
    }

}
