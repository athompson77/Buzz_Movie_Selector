package com.datapp.buzz_movieselector.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;

import com.datapp.buzz_movieselector.R;
import com.datapp.buzz_movieselector.model.DBHelper;
//import com.datapp.buzz_movieselector.model.UserManager;
import com.datapp.buzz_movieselector.model.User;

import java.sql.SQLException;

/**
 * Created by ale_patron on 2/2/16.
 *
 * Class to handle logging in of a user.
 */
public class LoginActivity extends AppCompatActivity {
    private DBHelper mydb;
    private String userName;
    private int attempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);

        mydb = new DBHelper(this);
        mydb.open();
    }

    /**
     * Method called when login button is pressed. Checks if the required fields are filled
     * and checks them against fields in the database. Calls takeToDashboard().
     *
     * @param v view onto which the press is being submitted to
     */
    public void onLoginButtonPressed(View v) throws SQLException {
        Log.d("LOGIN ACTIVITY", "Login Button Pressed");
        //AuthenticationFacade af = new UserManager();
        EditText nameBox = (EditText) findViewById(R.id.email);
        EditText passBox = (EditText) findViewById(R.id.password);
        if (TextUtils.isEmpty(nameBox.getText().toString())) {
            nameBox.setError("Please enter your username.");
        }
        if (TextUtils.isEmpty(passBox.getText().toString())) {
            passBox.setError("Please enter your password.");
        }
        Context context = getApplicationContext();

        this.userName = nameBox.getText().toString();
        String userPassword = passBox.getText().toString();
        int loginSuccess = mydb.validateLogin(userName, userPassword);

        if (loginSuccess == 1 && (mydb.isActive(userName) || mydb.isAdmin(userName))) {
            // Correct login combination, take them inside the application!
            Toast.makeText(context, "Login Success!", Toast.LENGTH_SHORT).show();
            takeToDashboard();
        } else {
            // let's handle possible errors and let the user know.
            CharSequence text;
            if (loginSuccess == -1) {
                // Incorrect login combination. Username
                text = "Login Failure! Username not registered!";
            } else {
                // Guaranteed that the username exists...
                if (this.attempts >= 2) {
                    mydb.lockUser(userName); // System locks the user.
                    text = "Too many attempts... Your account has been locked!";
                } else {
                    if (loginSuccess == 0) {
                        // Incorrect login combination. Username exists, but incorrect password.
                        this.attempts++;
                        text = "Login Failure! Incorrect password!";
                    } else {
                        if (mydb.isBanned(userName)) {
                            // Correct login combination, but account is banned.
                            text = "Login Failure! Your account is banned!";
                        } else if (mydb.isLocked(userName)) {
                            // Correct login combination, but account is locked.
                            text = "Login Failure! Your account is locked!";
                        } else {
                            text = "Oops! Something went wrong, please try again!";
                        }
                    }
                }
            }
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method called from onLoginButtonPressed(). If the user information has been entered correctly
     * the user will be taken to the dashboard.
     */
    public void takeToDashboard() throws SQLException {
        Intent intent;
        // Checks for admin status
        if (mydb.isAdmin(userName)) {
            intent = new Intent(this, AdminDashActivity.class);
        } else {
            intent = new Intent(this, DashboardActivity.class);
            // Passing current user to dashboard
            Cursor cursor = mydb.getMajor(userName);
            User user = new User(cursor);
            intent.putExtra("user", user);
        }
        if (OpeningScreenActivity.open != null) {
            OpeningScreenActivity.open.finish();
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        mydb.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //mydb.close();
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

    /**
     * Method that returns the database created in this activity;
     * @return the created database of users
     */
    public DBHelper getMydb() {
        return this.mydb;
    }

}
