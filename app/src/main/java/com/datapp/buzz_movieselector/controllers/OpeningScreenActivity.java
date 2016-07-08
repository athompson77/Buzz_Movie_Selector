package com.datapp.buzz_movieselector.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.datapp.buzz_movieselector.R;



/**
 * Created by pj on 2/2/16.
 *
 * The home screen that handles register presses or log in presses
 */
public class OpeningScreenActivity extends AppCompatActivity {
    public static Activity open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        open = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("**MYAPP**", "Pausing the opening screen");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("**MYAPP**", "Resuming the opening screen");
        System.out.println("resuming");
    }

    /**
     * Handles when login button is pressed and takes you to dashboard if login successful
     */
    public void onLoginButtonClicked(View w) {
        Log.d("**MYAPP**", "Login button pressed!");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Handles when register button is pressed and takes you to register screen
     *
     */
    public void onRegisterButtonClicked(View v) {
        Log.d("**MYAPP**", "Register button pressed!");
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opening_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

}
