package com.datapp.buzz_movieselector.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.datapp.buzz_movieselector.R;

import java.util.ArrayList;

public class UsersArrayAdapter extends ArrayAdapter<User> {

    /**
     * Creates a UsersArrayAdapter object.
     * @param context the current context window of the application
     * @param users arraylist of the possible users
     */
    public UsersArrayAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get Rating object at this position
        User currentUser = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }
        // Look up text views for data population
        TextView user = (TextView) convertView.findViewById(R.id.user);
        TextView numberRating = (TextView) convertView.findViewById(R.id.status);
        // Populate data using Rating object
        user.setText("User: "+ currentUser.getName());
        numberRating.setText("Status: " + currentUser.getStatus());
        // Return completed view
        return convertView;
    }
}
