package com.datapp.buzz_movieselector.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import com.datapp.buzz_movieselector.R;
import java.util.List;


public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    /**
     * Creates a MovieArrayAdapter object.
     * @param context context where adapter is created
     * @param movies movies to insert into adapter
     */
    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get Rating object at this position
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }
        // Look up views for data population
        ImageView poster = (ImageView) convertView.findViewById(R.id.poster);
        TextView movieName = (TextView) convertView.findViewById(R.id.movie);
        TextView year = (TextView) convertView.findViewById(R.id.year);
        // Populate data using Movie object
        movie.loadImageFromURL(movie.getImage(), poster);
        movieName.setText(movie.getName());
        year.setText(Integer.toString(movie.getYear()));
        // Return completed view
        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }
}
