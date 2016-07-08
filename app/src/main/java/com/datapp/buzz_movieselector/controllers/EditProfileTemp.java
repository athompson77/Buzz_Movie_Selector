package com.datapp.buzz_movieselector.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
//import android.widget.AdapterView;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.database.Cursor;

import com.datapp.buzz_movieselector.R;
import com.datapp.buzz_movieselector.model.DBHelper;
import com.datapp.buzz_movieselector.model.User;

import java.sql.SQLException;

/**
 * Created by alex on 2/12/16
 *
 * Class to handle the editing of the user's profile. Updates current user and data base
 * accordingly based on what changes have been made.
 *
 * TO BE USED INSTEAD OF EditProfileActivity UNTIL FURTHER NOTICE
 */
public class EditProfileTemp extends AppCompatActivity {

    private DBHelper mydb;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mydb = new DBHelper(this);
        mydb.open();
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_temp);

        //instantiate spinner object (holds options for major)
        Spinner spinner = (Spinner) findViewById(R.id.userMajor);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.majors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //sets selection to user's current major
        spinner.setSelection(adapter.getPosition(user.getMajor()));
    }

    /**
     * Method called when update button is pressed. Evaluates all changes made
     * to username, password, and major and updates database.
     *
     * @param v view onto which the press is being submitted to
     */
    public void onSubmitPress(View v) throws SQLException {
        EditText nameBox = (EditText) findViewById(R.id.userName);
        EditText passBox = (EditText) findViewById(R.id.userPassword);
        EditText intBox = (EditText) findViewById(R.id.userInt);
        boolean nameEmpty = TextUtils.isEmpty(nameBox.getText().toString());
        boolean passEmpty = TextUtils.isEmpty(passBox.getText().toString());
        boolean interestsEmpty = TextUtils.isEmpty(intBox.getText().toString());

        //creates the spinner device for choosing majors
        Spinner majorSpinner=(Spinner) findViewById(R.id.userMajor);
        String major = majorSpinner.getSelectedItem().toString();
        user.setMajor(major);

        CharSequence text = null;
        String oldUser = user.getName();
        if (!nameEmpty) {
            if (mydb.availableUsername(nameBox.getText().toString())) {
                if (!passEmpty) {
                    user.setPassword(passBox.getText().toString());
                }
                if (!interestsEmpty) {
                    user.setInterests(intBox.getText().toString());
                }
                user.setName(nameBox.getText().toString());
                text = "Updates Made";
            } else {
                text = "Username already taken. No changes made";
            }
        } else if (!passEmpty) {
            if (!interestsEmpty) {
                user.setInterests(intBox.getText().toString());
            }
            user.setPassword(passBox.getText().toString());
            text = "Updates Made";
        } else if (!interestsEmpty) {
            user.setInterests(intBox.getText().toString());
            text = "Updates Made";
        }
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast t = Toast.makeText(context, text, duration);
        t.show();

        //Passing current user to dashboard. Hardcode for now
        Intent intent = new Intent(this, ProfileActivity.class);

        // Update the data base with new changes
        Cursor cursor = mydb.getMajor(oldUser);
        intent.putExtra("user", user);
        if( cursor != null && cursor.moveToFirst()){
            int id =  Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            mydb.updateUser(id,user.getName(), user.getPassword(), user.getMajor(), user.getInterests());
            startActivity(intent);
            cursor.close();
            finish();
        } else {
            Toast error = Toast.makeText(context, "Update failed, try again", duration);
            error.show();
        }

    }

    @Override
    public void onBackPressed(){
        // hardcode back, need to fix
        Intent profile = new Intent(this, ProfileActivity.class);
        profile.putExtra("user", this.user);
        startActivity(profile);
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
     * Testing purpose method to get user object
     * @return current user object
     */
    public User getCurrentUser() {
        return this.user;
    }

    /**
     * Testing purpose method to get Dataebase helper
     * @return current DB helper instance
     */
    public DBHelper getMydb() {
        return this.mydb;
    }

    /**
     * Testing purpose method to temporarily set the user
     * @param newUser user to set to
     */
    public void setUser(User newUser) {
        this.user = newUser;
    }
}
