package com.example.android.popularmoviestest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ToM on 16/11/2015.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();


    /** Custom constructor
     *
     * @param context The current context used to inflate the layout file.
     * @param movieList A list of Movie Posters objects to display in a list.
     */
    public MovieAdapter(Activity context, ArrayList<Movie> movieList) {
        //Here, we initialize the ArrayAdapter's internal storage for the context of the list.
        super(context, 0, movieList);

    }
    //Method for adding the movie results
    //public void addAll(MovieInfo movieInfo) {
      //  for (Movie movieData : movieInfo.getInfos())
      //  {
      //      add(new Movie(movieData));
      //  }
    //}

    /**Provides a view for an AdapterView
     *
     * @param position The AdapterView position that is requesting a View
     * @param convertView The recycle view to populate
     * @param parent The parent ViewGroup that is used for inflation.
     * @return the View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Gets the Movie Posters object from the ArrayAdapter at the appropriate position.
        Movie movie = getItem(position);

        //Adapters recycle views to AdapterViews.
        //If this is a new View object we're getting, then inflate the layout.
        //If not, this view already has the layout inflated from a previous call to getView,
        //and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_image, parent, false);

        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_imageview);
        iconView.setImageResource(Integer.parseInt(movie.posterPath));
        Picasso.with(getContext()).load(movie.posterPath).fit().into(iconView);

        return convertView;
    }

    public void add(String s) {
    }
}
