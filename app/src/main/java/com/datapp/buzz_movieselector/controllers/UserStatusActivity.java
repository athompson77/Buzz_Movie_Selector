package com.datapp.buzz_movieselector.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.datapp.buzz_movieselector.R;
import com.datapp.buzz_movieselector.model.DBHelper;
import com.datapp.buzz_movieselector.model.User;

import android.widget.Switch;
import android.widget.TextView;

public class UserStatusActivity extends AppCompatActivity {

    private User currentUser;
    private DBHelper usersDB;
    private Switch bannedSwitch;
    private TextView bannedSwitchStatus;
    private Switch lockedSwitch;
    private TextView lockedSwitchStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting dimensions for pop up view
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .5));

        usersDB = new DBHelper(this);
        usersDB.open();
        final Intent intent = getIntent();
        this.currentUser = (User) intent.getSerializableExtra("currentUser");
        setContentView(R.layout.activity_user_status);

        bannedSwitch = (Switch) findViewById(R.id.bannedSwitch);

        //cannot ban admin
        if (currentUser.getName().equals("ADMIN")) {
            bannedSwitch.setClickable(false);
        }
        //set ban switch to user's current ban status
        if (currentUser.isBanned()) {
            bannedSwitch.setChecked(true);
            bannedSwitch.setText("Banned    ");
        } else {
            bannedSwitch.setChecked(false);
            bannedSwitch.setText("Active    ");
        }

        bannedSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //if ban switched from active to banned
                    bannedSwitch.setText("Banned    ");
                    usersDB.banUser((currentUser.getName()));
                } else {
                    //if ban switched from banned to active
                    bannedSwitch.setText("Active    ");
                    usersDB.activateUser((currentUser.getName()));
                }
            }
        });

        lockedSwitch = (Switch) findViewById(R.id.lockedSwitch);
        
        //set lock switch to users current lock status
        if (currentUser.isLocked()) {
            lockedSwitch.setChecked(true);
            lockedSwitch.setText("Locked out    ");
        } else {
            lockedSwitch.setChecked(false);
            lockedSwitch.setText("Unlocked    ");
            //cannot lock a user
            lockedSwitch.setClickable(false);
        }

        lockedSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!(isChecked)){
                    //if switched from locked to unlocked
                    lockedSwitch.setText("Unlocked    ");
                    lockedSwitch.setClickable(false);
                    usersDB.activateUser((currentUser.getName()));
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(currentUser.getName());
//        setSupportActionBar(toolbar);

    }

}
