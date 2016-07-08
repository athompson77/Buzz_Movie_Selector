package com.datapp.buzz_movieselector.controllers;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.datapp.buzz_movieselector.model.DBHelper;
import com.datapp.buzz_movieselector.model.Movie;

import com.datapp.buzz_movieselector.R;
import com.datapp.buzz_movieselector.model.Rating;
import com.datapp.buzz_movieselector.model.RatingDatabaseHelper;
import com.datapp.buzz_movieselector.model.RatingTable;
import com.datapp.buzz_movieselector.model.RatingsArrayAdapter;
import com.datapp.buzz_movieselector.model.User;

import java.util.ArrayList;


public class MovieDetailActivity extends AppCompatActivity {

    private Movie currentMovie;
    private User currentUser;
    private String currentMajor;
    private RatingTable ratingDB;
    private DBHelper usersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CONNECT to Ratings Database
        ratingDB = new RatingTable(this);
        usersDB = new DBHelper(this);
        ratingDB.open();
        usersDB.open();

        // GET CURRENT Movie OBJECT
        final Intent intent = getIntent();
        this.currentMovie = (Movie) intent.getSerializableExtra("currentMovie");
        this.currentUser = (User) intent.getSerializableExtra("currentUser");
        this.currentMajor = intent.getStringExtra("currentMajor");

        //SET Toolbar with Movie.getName()
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView imageView = (ImageView) findViewById(R.id.poster);
        TextView averageRating = (TextView) findViewById(R.id.averageRating);
        TextView released = (TextView) findViewById(R.id.released);
        String overallRating = String.valueOf(ratingDB.getAvgRatingbyMajor(currentMovie.getId(), currentMajor));
        currentMovie.loadImageFromURL(currentMovie.getImage(), imageView);
        toolbar.setTitle(currentMovie.getName());
        averageRating.setText(overallRating);
        released.setText("Released in: " + currentMovie.getYear());
        setSupportActionBar(toolbar);
        populate();
    }
    @Override
    protected void onResume() {
        ratingDB.open();
        super.onResume();
        populate();
    }

    /**
     * Populates view with current movie ratings, if any exist
     */
    private void populate() {
        //GET RATINGS ASSOCIATED TO MOVIE
        ArrayList<Rating> ratings = new ArrayList<>();
        Cursor cursor = ratingDB.getRatingsbyMovie(currentMovie.getId(), currentMajor);
        double avgRating = ratingDB.getAvgRatingbyMajor(currentMovie.getId(), currentMajor);

        while (cursor != null && cursor.moveToNext()) {
            int userId = cursor.getInt(cursor.getColumnIndex(RatingDatabaseHelper.COLUMN_USER));
            User user = new User(usersDB.getData(userId));
            Rating rating = new Rating(cursor);
            rating.setUser(user);
            rating.setMovie(currentMovie);
            ratings.add(rating);
        }
        TextView averageRating = (TextView) findViewById(R.id.averageRating);
        String overallRating = String.valueOf(avgRating);
        averageRating.setText(overallRating);

        ListView list = (ListView)findViewById(R.id.list);
        // Using custom adapter (RatingsArrayAdapter)
        RatingsArrayAdapter ratingRatingsArrayAdapter = new RatingsArrayAdapter(this, ratings);
        list.setAdapter(ratingRatingsArrayAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RatingActivity.class);
                intent.putExtra("currentMovie", currentMovie);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        ratingDB.close();
        super.onPause();
    }
}
