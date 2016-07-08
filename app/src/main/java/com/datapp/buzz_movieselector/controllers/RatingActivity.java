package com.datapp.buzz_movieselector.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.datapp.buzz_movieselector.model.RatingTable;
import com.datapp.buzz_movieselector.model.User;
import com.datapp.buzz_movieselector.R;
import com.datapp.buzz_movieselector.model.Movie;
import android.util.Log;

import java.sql.SQLException;

public class RatingActivity extends AppCompatActivity {

    private Movie ratingMovie;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .5));


        setContentView(R.layout.activity_rating);
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("currentUser");
        Log.d(user.getName(), "USER");

        this.ratingMovie = (Movie) intent.getSerializableExtra("currentMovie");
        setTitle("New Rating");

        Spinner spinner = (Spinner) findViewById(R.id.ratingSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ratings_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Method called when update button is pressed. Evaluates all changes made
     * to username, password, and major and updates database.
     *
     * @param v view onto which the press is being submitted to
     */
    public void onSubmitPress(View v) throws SQLException {
        Context context = getApplicationContext();
        RatingTable ratingDB = new RatingTable(context);
        ratingDB.open();

        EditText commentBox = (EditText) findViewById(R.id.userComment);
        String comment = commentBox.getText().toString();
        boolean commentEmpty = TextUtils.isEmpty(commentBox.getText().toString());

        Spinner ratingSpinner = (Spinner) findViewById(R.id.ratingSpinner);
        String ratingStr = ratingSpinner.getSelectedItem().toString();
        int rating = -1;
        if (ratingStr.equals("-Please Select a Rating-")) {
            Toast t = Toast.makeText(context, "Please select a rating", Toast.LENGTH_SHORT);
            t.show();
        } else {
            rating = Integer.parseInt(ratingStr);
        }
        Toast t;
        if (!commentEmpty && rating != -1) {
             if (ratingDB.insertRating(ratingMovie, user, comment, rating)){
                 t = Toast.makeText(context, "Rating successfully added!", Toast.LENGTH_SHORT);
             } else {
                 t = Toast.makeText(context, "Unable to add rating. Please try again.", Toast.LENGTH_SHORT);
             }
            t.show();
            finish();
        } else if (rating == -1){
            t = Toast.makeText(context, "Please select a rating!", Toast.LENGTH_SHORT);
        } else {
            t = Toast.makeText(context, "Please make a comment!", Toast.LENGTH_SHORT);
        }
        t.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return true;
    }
}
