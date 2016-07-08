package com.datapp.buzz_movieselector.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.datapp.buzz_movieselector.R;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.content.Intent;
import android.database.Cursor;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.text.TextUtils;
import android.content.Context;
import android.widget.Spinner;
import android.widget.Toast;

import com.datapp.buzz_movieselector.model.DBHelper;
import com.datapp.buzz_movieselector.model.User;

import java.sql.SQLException;

/**
 * Created by ale_patron on 2/2/16.
 *
 * Handles registration
 */
public class RegisterActivity extends AppCompatActivity {
    private DBHelper mydb;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Register");
        setSupportActionBar(toolbar);

        //instantiate spinner object (holds options for major)
        Spinner spinner = (Spinner) findViewById(R.id.registerMajor);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.register_majors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        mydb = new DBHelper(this);
        mydb.open();
    }

    /**
     * Method called when the register button is pressed. Handles validating and
     * taking to dashboard.
     *
     * @param v view going to be taken to
     */
    public void onRegisterButtonPressed(View v) {
        Log.d("REGISTER ACTIVITY", "Register button pressed");

        //UserManager um = new UserManager();
        EditText nameBox = (EditText) findViewById(R.id.user);
        EditText passBox = (EditText) findViewById(R.id.password);

        //CharSequence text;
        boolean empty = false;
        if (TextUtils.isEmpty(nameBox.getText().toString())) {
            empty = true;
            nameBox.setError("Please enter a username.");
        }
        if (TextUtils.isEmpty(passBox.getText().toString())) {
            empty = true;
            passBox.setError("Please enter a password.");
        }

        String username = nameBox.getText().toString();
        String password = passBox.getText().toString();
        //creates the spinner device for choosing major
        Spinner majorSpinner=(Spinner) findViewById(R.id.registerMajor);
        String major = majorSpinner.getSelectedItem().toString();

        int duration = Toast.LENGTH_SHORT;
        Context context = getApplicationContext();

        if (major.equals("-Please Select a Major-")) {
            empty = true;
            Toast t = Toast.makeText(context, "Please select a major", duration);
            t.show();
        }

        this.userName = nameBox.getText().toString();
        String userPassword = passBox.getText().toString();

        if(!empty) {
            if (! mydb.insertUser(username, password, major)) {
                Toast.makeText(context, "Username taken, please choose a different one", duration).show();
            } else {
                Toast.makeText(context, "Registered", duration).show();
                try {
                    takeToDashboard(true);
                } catch (SQLException e) {
                    Toast.makeText(context, "Failed to register", duration).show();
                }
            }
        }
    }

    /**
     * Method called from onRegisterButtonPressed(). If the user information has been successfully
     * registered the user will be taken to the dashboard.
     *
     * @param loginSuccess whether or not the User has been verified by the database
     */
    public void takeToDashboard(boolean loginSuccess) throws SQLException {
        if (loginSuccess) {
            Intent intent = new Intent(this, DashboardActivity.class);

            // Passing current user to dashboard
            Cursor cursor = mydb.getMajor(userName);
//            String major = cursor.getString(cursor.getColumnIndex("major"));
            User user = new User(cursor);
            user.setId(mydb.getId(userName));
            intent.putExtra("user", user);

            OpeningScreenActivity.open.finish();
            startActivity(intent);
            finish();
        }
    }

    @Override
         protected void onResume() {
        mydb.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mydb.close();
        super.onPause();
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

    public DBHelper getMydb() {
        return this.mydb;
    }
}
