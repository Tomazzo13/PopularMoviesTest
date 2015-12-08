package com.example.android.popularmoviestest;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a list view with movie posters.
 */
public class MovieFragment extends Fragment {

    private MovieAdapter movieAdapter;

    private ArrayList<Movie> movieList;

    //Create some dummy data with movie posters

    //Movie[] movies = {

    //      new Movie(R.drawable.alien),
    //    new Movie(R.drawable.antman),
    //  new Movie(R.drawable.avatar),
    //new Movie(R.drawable.avengers),
    //};

    public MovieFragment() {
    }
    //Save the state of the app when rotating the device so that the data don't get lost in the process.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieAdapter = new MovieAdapter(getActivity(), movieList);

        //Get a reference to the GridView and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movie_gridview);
        gridView.setAdapter(movieAdapter);

        movieList = new ArrayList<Movie>();

        updateMovie();

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    public void updateMovie() {
        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        fetchMovieTask.execute();

    }



    public class FetchMovieTask extends AsyncTask<String, Void, JSONArray> {


        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();


        @Override
        protected JSONArray doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieDbJsonStr = null;

            try {
                // Construct the URL for the Movie query
                final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
                String APPID_PARAM = "api_key";

                String api_key = "ee7a555268688da2f374d97ceca7faa6";

                Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, api_key)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI: " + builtUri.toString());

                // Create the request to Movie, and open the connection
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
                movieDbJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
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

                try {
                    return getMovieFromJson(movieDbJsonStr);

                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }

                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            if (result != null) {
                movieAdapter.clear();
                for (int i = 0; i < movieList.size(); ++i) {
                    movieAdapter.add(movieList.get(i));
                }
            }
        }
    }


        private JSONArray getMovieFromJson(String movieJsonStr) throws JSONException {

            //These are the names of the JSON objects that need to be extracted.
            final String TMDB_ARRAY = "results";
            final String TMDB_TITLE = "original_title";
            final String TMDB_SUMMARY = "overview";
            final String TMDB_POSTER = "poster_path";
            final String TMDB_POPULARITY = "popularity";
            final String TMDB_VOTE_AVERAGE = "vote_average";
            final String TMDB_RELEASE_DATE = "release_date";


            JSONObject movieDbJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieDbJson.getJSONArray(TMDB_ARRAY);

            String posterBaseUrl = "http://image.tmdb.org/t/p/w185";


            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieInfo = movieArray.getJSONObject(i);


                String movieTitle = movieInfo.getString(TMDB_TITLE);
                String posterPath = posterBaseUrl + movieInfo.getString(TMDB_POSTER);
                String overview = movieInfo.getString(TMDB_SUMMARY);
                String popularity = movieInfo.getString(TMDB_POPULARITY);
                String voteAverage = movieInfo.getString(TMDB_VOTE_AVERAGE);
                String releaseDate = movieInfo.getString(TMDB_RELEASE_DATE);

            }

            return movieArray;
        }



}






