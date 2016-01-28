package com.example.android.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.android.movies.adapter.MovieDetailAdapter;
import com.example.android.movies.model.MovieDetail;
import com.example.android.movies.util.MovieJsonResponse;
import com.example.android.movies.util.TMDbService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * A fragment to render movie posters in GridView
 */
public class MainActivityFragment extends Fragment {

    private ArrayAdapter<MovieDetail> movieDetailsAdapter;
    ArrayList<MovieDetail> movieDetailList = null;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        //if movieDetailList is not null than use it to create adapter
        if(movieDetailList != null && movieDetailList.size() > 0){
            movieDetailsAdapter = new MovieDetailAdapter(getActivity(), movieDetailList);
        }else {
            //create adapter with blank list
            movieDetailsAdapter = new MovieDetailAdapter(getActivity(), new ArrayList<MovieDetail>());
        }

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(movieDetailsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDetail movieDetail = movieDetailsAdapter.getItem(position);
                Intent intent = new Intent(getActivity(),MovieDetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movieDetail);
                startActivity(intent);
            }
        });

        return rootView;
    }

    /**
     * Call Async task to get movie data from network
     * by passing sorting type
     */
    private void fetchMovies() {
        FetchMovieDetail fetchMovieDetail = new FetchMovieDetail();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by = prefs.getString(getString(R.string.pref_sort_by_key),
                getString(R.string.pref_sort_by_popularity));
        fetchMovieDetail.execute(sort_by);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movieDetails")) {
            fetchMovies();
        }
        else {
            movieDetailList = savedInstanceState.getParcelableArrayList("movieDetails");
        }
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movieDetails", movieDetailList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart(){
        super.onStart();
        fetchMovies();
    }

    public class FetchMovieDetail extends AsyncTask<String,Void,List<MovieDetail>> {

        private final String LOG_TAG = FetchMovieDetail.class.getSimpleName();

        private List<MovieDetail> getMovieDataFromJson(String moviesJsonStr)throws JSONException{

            movieDetailList = new ArrayList<MovieDetail>();

            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_ID = "id";
            final String TMDB_TITLE = "title";
            final String TMDB_POPULARITY = "popularity";
            final String TMDB_VOTE_COUNT = "vote_count";
            final String TMDB_VOTE_AVG = "vote_average";
            final String TMDB_RELEASE_DATE = "release_date";
            final String TMDB_OVERVIEW = "overview";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);

            for(int i = 0; i < moviesArray.length(); i++) {
                MovieDetail movieDetail = new MovieDetail();

                // Get the JSON object representing one movie
                JSONObject movie = moviesArray.getJSONObject(i);
                movieDetail.setId(movie.getInt(TMDB_ID));
                movieDetail.setPosterPath(movie.getString(TMDB_POSTER_PATH));
                movieDetail.setTitle(movie.getString(TMDB_TITLE));
                movieDetail.setPopularity(Float.valueOf(movie.getString(TMDB_POPULARITY)));
                movieDetail.setVoteCount(movie.getLong(TMDB_VOTE_COUNT));
                movieDetail.setVoteAvg(Float.valueOf(movie.getString(TMDB_VOTE_AVG)));
                movieDetail.setReleaseDate(movie.getString(TMDB_RELEASE_DATE));
                movieDetail.setMovieOverview(movie.getString(TMDB_OVERVIEW));
                // add movieDetail to list
                movieDetailList.add(movieDetail);
            }
            return movieDetailList;
        }

        @Override
        protected List<MovieDetail> doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            /*HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String MOVIEDB_BASE_URL ="http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String APIKEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(APIKEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to themoviedb.org, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movies data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }*/

            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://api.themoviedb.org/3/")
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

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieDetail> moviesList) {
            if (moviesList != null && moviesList.size() > 0) {
                movieDetailsAdapter.clear();
                for(MovieDetail movieDetail : moviesList) {
                    movieDetailsAdapter.add(movieDetail);
                }
                // New data is back from the server.  Hooray!
            }
        }
    }
}
