package com.datapp.buzz_movieselector.model;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.datapp.buzz_movieselector.R;
import java.util.ArrayList;

public class RatingsArrayAdapter extends ArrayAdapter<Rating> {

    /**
     * Creates a RatingsArrayAdapter object.
     * @param context the current context of the application
     * @param ratings arraylist of the different ratings
     */
    public RatingsArrayAdapter(Context context, ArrayList<Rating> ratings) {
        super(context, 0, ratings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get Rating object at this position
        Rating rating = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rating_item, parent, false);
        }
        // Look up text views for data population
        TextView user = (TextView) convertView.findViewById(R.id.user);
        TextView numberRating = (TextView) convertView.findViewById(R.id.rating);
        TextView comment = (TextView) convertView.findViewById(R.id.userComment);
        TextView major = (TextView) convertView.findViewById(R.id.major);
        // Populate data using Rating object
        user.setText("User: "+ rating.getUser().getName());
        numberRating.setText(rating.getRating() + "/5");
        comment.setText(rating.getComment());
        major.setText("Major: " + rating.getUser().getMajor());
        // Return completed view
        return convertView;
    }
}
