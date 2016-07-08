package com.datapp.buzz_movieselector.controllers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.datapp.buzz_movieselector.R;
import com.datapp.buzz_movieselector.model.DBHelper;
import com.datapp.buzz_movieselector.model.User;

import org.junit.Rule;
import org.junit.Test;

import java.sql.SQLException;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

/**
 * Created by pj on 4/3/16.
 */
public class EditProfileTempTest {

    private static String OUSER = "Buzz";
    private static String OPASS = "thwg";
    private static String OMAJOR = "CS";
    private static String OINTERESTS = "BuzzBuzzBuzz";
    private static String USER = "georgeP";
    private static String PASS = "gatech";
    private static String MAJOR = "Math";
    private static String INTERESTS = "Running JUnits!";

    @Rule
    public ActivityTestRule<EditProfileTemp> mActivityRule =
            new ActivityTestRule<EditProfileTemp>(EditProfileTemp.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent();
                    User user = new User(OUSER, OPASS, OMAJOR, OINTERESTS);
                    result.putExtra("user", user);
                    return result;
                }
            };

    public void setUser() {
        User user = new User(OUSER, OPASS, OMAJOR, OINTERESTS);
        DBHelper db = mActivityRule.getActivity().getMydb();
        db.insertUser(user.getName(), user.getPassword(), user.getMajor());
    }

    public void setDuoUser() {
        DBHelper db = mActivityRule.getActivity().getMydb();
        User user = new User(OUSER, OPASS, OMAJOR, OINTERESTS);
        User user2 = new User("Wade Wilson", "dp", "Business");
        db.insertUser(user.getName(), user.getPassword(), user.getMajor());
        db.insertUser(user2.getName(), user2.getPassword(), user2.getMajor());
    }

    /**
     * Method to test edit name to valid name
     */
    @Test
    public void editNameValid() {
        setUser();
        // Input new username
        onView(withId(R.id.userName)).perform(typeText(USER), closeSoftKeyboard());
        // Click Button to update profile
        onView(withId(R.id.profile_update_button)).perform(click());

        // Grab DB Helper with database
        DBHelper database = mActivityRule.getActivity().getMydb();
        Cursor cursor = null;
        try {
            cursor = database.getMajor(USER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Construct user with that information
        User currUser = new User(cursor);
        // Assert equals on all aspects, User name should have changed and rest stayed same
        assertEquals("Incorrect username in database", USER, currUser.getName());
        assertEquals("Incorrect password in database", OPASS, currUser.getPassword());
        assertEquals("Incorrect major in database", OMAJOR, currUser.getMajor());
        assertEquals("Incorrect interests in database", OINTERESTS, currUser.getInterests());

        User activityUser = mActivityRule.getActivity().getCurrentUser();
        assertEquals("Incorrect username in user object", USER, activityUser.getName());
        assertEquals("Incorrect password in user object", OPASS, activityUser.getPassword());
        assertEquals("Incorrect major in user object", OMAJOR, activityUser.getMajor());
        assertEquals("Incorrect interests in user object", OINTERESTS, activityUser.getInterests());

        database.deleteUser(database.getId(USER));
    }

    /**
     * Method to test edit password
     */
    @Test
    public void editPassword() {
        setUser();
        // Check pass input
        onView(withId(R.id.userPassword)).perform(typeText(PASS), closeSoftKeyboard());
        // Click Button to register
        onView(withId(R.id.profile_update_button)).perform(click());

        DBHelper database = mActivityRule.getActivity().getMydb();
        Cursor cursor = null;
        try {
            cursor = database.getMajor(OUSER);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User currUser = new User(cursor);
        // Assert equals on all aspects, Password should have changed and rest stayed same
        assertEquals("Incorrect username in database", OUSER, currUser.getName());
        assertEquals("Incorrect password in database", PASS, currUser.getPassword());
        assertEquals("Incorrect major in database", OMAJOR, currUser.getMajor());
        assertEquals("Incorrect interests in database", OINTERESTS, currUser.getInterests());

        User activityUser = mActivityRule.getActivity().getCurrentUser();
        assertEquals("Incorrect username in user object", OUSER, activityUser.getName());
        assertEquals("Incorrect password in user object", PASS, activityUser.getPassword());
        assertEquals("Incorrect major in user object", OMAJOR, activityUser.getMajor());
        assertEquals("Incorrect interests in user object", OINTERESTS, activityUser.getInterests());

        database.deleteUser(database.getId(OUSER));
    }

    /**
     * Method to test edit major
     */
    @Test
    public void editMajor() {
        setUser();
        // Check major input
        onView(withId(R.id.userMajor)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(MAJOR))).perform(click());
        // Click Button to register
        onView(withId(R.id.profile_update_button)).perform(click());

        DBHelper database = mActivityRule.getActivity().getMydb();
        Cursor cursor = null;
        try {
            cursor = database.getMajor(OUSER);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User currUser = new User(cursor);
        // Assert equals on all aspects, Major should have changed and rest stayed same
        assertEquals("Incorrect username in database", OUSER, currUser.getName());
        assertEquals("Incorrect password in database", OPASS, currUser.getPassword());
        assertEquals("Incorrect major in database", MAJOR, currUser.getMajor());
        assertEquals("Incorrect interests in database", OINTERESTS, currUser.getInterests());

        User activityUser = mActivityRule.getActivity().getCurrentUser();
        assertEquals("Incorrect username in user object", OUSER, activityUser.getName());
        assertEquals("Incorrect password in user object", OPASS, activityUser.getPassword());
        assertEquals("Incorrect major in user object", MAJOR, activityUser.getMajor());
        assertEquals("Incorrect interests in user object", OINTERESTS, activityUser.getInterests());

        database.deleteUser(database.getId(OUSER));
    }

    /**
     * Method to test for change in interests
     */
    @Test
    public void editInterests() {
        setUser();
        // Check user interests input
        onView(withId(R.id.userInt)).perform(typeText(INTERESTS), closeSoftKeyboard());
        // Click Button to register
        onView(withId(R.id.profile_update_button)).perform(click());

        DBHelper database = mActivityRule.getActivity().getMydb();
        Cursor cursor = null;
        try {
            cursor = database.getMajor(OUSER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User currUser = new User(cursor);
        // Assert equals on all aspects, Interests should have changed and rest stayed same
        assertEquals("Incorrect username in database", OUSER, currUser.getName());
        assertEquals("Incorrect password in database", OPASS, currUser.getPassword());
        assertEquals("Incorrect major in database", OMAJOR, currUser.getMajor());
        assertEquals("Incorrect interests in database", INTERESTS, currUser.getInterests());

        User activityUser = mActivityRule.getActivity().getCurrentUser();
        assertEquals("Incorrect username in user object", OUSER, activityUser.getName());
        assertEquals("Incorrect password in user object", OPASS, activityUser.getPassword());
        assertEquals("Incorrect major in user object", OMAJOR, activityUser.getMajor());
        assertEquals("Incorrect interests in user object", INTERESTS, activityUser.getInterests());

        database.deleteUser(database.getId(OUSER));
    }

    /**
     * Method to test trying to edit name to taken name in database.
     * User name should not change
     */
    @Test
    public void usernameTaken() {
       setDuoUser();
        // Input user already in database
        onView(withId(R.id.userName)).perform(typeText("Wade Wilson"), closeSoftKeyboard());
        // Click Button to update profile
        onView(withId(R.id.profile_update_button)).perform(click());

        DBHelper database = mActivityRule.getActivity().getMydb();
        Cursor cursor = null;
        try {
            cursor = database.getMajor(OUSER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User currUser = new User(cursor);

        assertEquals("Incorrect username in database, username changed", OUSER, currUser.getName());
        assertNotEquals("Username was changed", "Wade Wilson", currUser.getName());
        assertEquals("Incorrect password in database", OPASS, currUser.getPassword());
        assertEquals("Incorrect major in database", OMAJOR, currUser.getMajor());
        assertEquals("Incorrect interests in database", OINTERESTS, currUser.getInterests());

        User activityUser = mActivityRule.getActivity().getCurrentUser();
        assertEquals("Incorrect username in user object", OUSER, activityUser.getName());
        assertNotEquals("Username was changed", "Wade Wilson", activityUser.getName());
        assertEquals("Incorrect password in user object", OPASS, activityUser.getPassword());
        assertEquals("Incorrect major in user object", OMAJOR, activityUser.getMajor());
        assertEquals("Incorrect interests in user object", OINTERESTS, activityUser.getInterests());

        database.deleteUser(database.getId(OUSER));
        database.deleteUser(database.getId("Wade Wilson"));
    }

}