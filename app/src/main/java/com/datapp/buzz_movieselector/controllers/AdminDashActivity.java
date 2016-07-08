package com.datapp.buzz_movieselector.controllers;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.datapp.buzz_movieselector.R;
import com.datapp.buzz_movieselector.model.DBHelper;
import com.datapp.buzz_movieselector.model.User;
import com.datapp.buzz_movieselector.model.UsersArrayAdapter;

import java.util.ArrayList;

public class AdminDashActivity extends ListActivity {

    private DBHelper mydb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Users");
        toolbar.inflateMenu(R.menu.menu_admin_dash_activty);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_profile:
//                Intent profileIntent = new Intent(this, ProfileActivity.class);
//                profileIntent.putExtra("user", this.user);
//                startActivity(profileIntent);
//                finish();
                        return true;
                    case R.id.action_logout:
                        Intent intent = new Intent(getApplicationContext(), OpeningScreenActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    default:
                        return true;
                }
            }
        });
        mydb = new DBHelper(this);
        mydb.open();
        populate();
    }

    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
        User currentUser = (User) l.getItemAtPosition(position);
        Intent intent = new Intent(this, UserStatusActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }

    /**
     * Populates list view with list of app users and their status
     */
    private void populate() {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<String> usernames = mydb.getAllUsers();
        for (String user : usernames) {
            User currentUser = new User(user, null, null);
            currentUser.setStatus(mydb.getStatus(user));
            users.add(currentUser);
        }
        UsersArrayAdapter adapter = new UsersArrayAdapter(getApplicationContext(), users);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        mydb.open();
        super.onResume();
        populate();
    }

    @Override
    protected void onPause() {
        mydb.close();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        //now back button is disabled to avoid returning to login/home screen
    }
}
