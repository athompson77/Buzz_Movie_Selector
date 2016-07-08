package com.datapp.buzz_movieselector.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.datapp.buzz_movieselector.model.User;

import com.datapp.buzz_movieselector.R;

/**
 * Created by pj on 2/12/16
 *
 * Class to handle the making of a profile and displaying. The user's basic information
 * Links to edit profile for user to edit information.
 *
 */
public class ProfileActivity extends AppCompatActivity {

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Your Profile");

        final Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");


        TextView userName = (TextView) findViewById(R.id.textView2);
        String uName = "Username: " + user.getName();
        userName.setText(uName);

        TextView userPassword = (TextView) findViewById(R.id.textView5);
        String uPass = "Password: " + user.getPassword();
        userPassword.setText(uPass);

        TextView userMajor = (TextView) findViewById(R.id.textView3);
        String uMajor = "Major: " + user.getMajor();
        userMajor.setText(uMajor);

        TextView userInterests = (TextView) findViewById(R.id.userInterests);
        String uInterests = "Interests: " + user.getInterests();
        userInterests.setText(uInterests);

        // Making user to pass to edit
        final Intent editIntent = new Intent(this, EditProfileTemp.class);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        editIntent.putExtra("user", this.user);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(editIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        // hardcode back, need to fix
        Intent newDash = new Intent(this, DashboardActivity.class);
        newDash.putExtra("user", this.user);
        startActivity(newDash);
        finish();
    }

}
