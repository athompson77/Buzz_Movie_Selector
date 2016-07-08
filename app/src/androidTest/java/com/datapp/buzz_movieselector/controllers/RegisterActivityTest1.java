package com.datapp.buzz_movieselector.controllers;
import android.database.Cursor;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.Espresso;

import com.datapp.buzz_movieselector.R;
import com.datapp.buzz_movieselector.model.DBHelper;
import com.datapp.buzz_movieselector.model.User;

import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by pj on 4/3/16.
 */
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest1 {

    private static String USER = "George P";
    private static String PASS = "gatech";
    private static String MAJOR = "Math";
    private static String INTERESTS = "N/A";

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);
    public ActivityTestRule<OpeningScreenActivity> open = new ActivityTestRule<>(OpeningScreenActivity.class);

    @Test
    public void newUser() {
        // Check user name input
        onView(withId(R.id.user)).perform(typeText(USER), closeSoftKeyboard());
        // Check pass input
        onView(withId(R.id.password)).perform(typeText(PASS), closeSoftKeyboard());
        // Check major input
        onView(withId(R.id.registerMajor)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(MAJOR))).perform(click());
        // Click Button to register
        onView(withId(R.id.email_sign_in_button)).perform(click());

        // Grab user object in dashboard and check username, pass, major, and interests
        DBHelper database = mActivityRule.getActivity().getMydb();
        Cursor cursor = null;
        try {
            cursor = database.getMajor(USER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User currUser = new User(cursor);
        assertEquals("Incorrect username in database", USER, currUser.getName());
        assertEquals("Incorrect password in database", PASS, currUser.getPassword());
        assertEquals("Incorrect major in database", MAJOR, currUser.getMajor());
        assertEquals("Incorrect default interests in database", INTERESTS, currUser.getInterests());
        database.deleteUser(database.getId(USER));
    }
}