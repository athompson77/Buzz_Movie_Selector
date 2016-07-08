package com.datapp.buzz_movieselector.controllers;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.datapp.buzz_movieselector.R;

import com.datapp.buzz_movieselector.model.DBHelper;
import com.datapp.buzz_movieselector.model.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Alex on 4/5/2016.
 * Tests that the user is being taken to the correct dashboard based on login credentials.
 */
@RunWith(AndroidJUnit4.class)
public class TakeToDashboardTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private DBHelper database;
    private static String REG_USER = "test";
    private static String REG_PASS = "pass";
    private static String ADMIN_USER = "ADMIN";
    private static String ADMIN_PASS = "DATAPP";

    public TakeToDashboardTest() {
        super(LoginActivity.class);
    }
    private LoginActivity mActivityRule;
    private Instrumentation mInstrumentation;


    /**
     * Does necessary setup of the activity and instrumentation objects
     * as well as add the temporary user to the database in order to check credentials during login
     */
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        setActivityInitialTouchMode(true);
        mActivityRule = getActivity();
        mInstrumentation = getInstrumentation();
        database = mActivityRule.getMydb();
        User user = new User(REG_USER, REG_PASS, "CS", "JUnit");
        database.insertUser(user.getName(), user.getPassword(), user.getMajor());
    }

    /**
     * Checks that the admin user is taken to the AdminDashActivity
     */
    @Test
    public void testIsAdmin() {
        //admin user is always in database, no need to create or delete for testing purposes

        //input the one and only accepted admin username
        onView(withId(R.id.email)).perform(typeText(ADMIN_USER), closeSoftKeyboard());
        //input the one and only accepted admin password
        onView(withId(R.id.password)).perform(typeText(ADMIN_PASS), closeSoftKeyboard());

        // Set up Activity Monitor
        // Every time admindash activity called it keeps track in hit counter
        Instrumentation.ActivityMonitor adminDashActivityMonitor =
                mInstrumentation.addMonitor(AdminDashActivity.class.getName(),
                        null, false);

        // Click login button
        TouchUtils.clickView(this, mActivityRule.findViewById(R.id.email_sign_in_button));

        // Wait for the Activity to Load within 10000 milliseconds
        AdminDashActivity receiverActivity = (AdminDashActivity)
                adminDashActivityMonitor.waitForActivityWithTimeout(10000);

        // Check that an instance of AdminDashActivity was created within 10000 milliseconds
        assertNotNull("Dashboard is null", receiverActivity);

        // Check the adminDash activity has loaded
        assertEquals("Dashboard Activity has not been called",
                1, adminDashActivityMonitor.getHits());

        // Remove the Activity Monitor
        getInstrumentation().removeMonitor(adminDashActivityMonitor);
    }

    /**
     * Checks that a regular user is taken to the (regular) DashboardActivity
     */
    @Test
    public void testIsUser() {
        //input a valid username
        onView(withId(R.id.email)).perform(typeText(REG_USER), closeSoftKeyboard());
        //input a valid matching password
        onView(withId(R.id.password)).perform(typeText(REG_PASS), closeSoftKeyboard());
        Espresso.closeSoftKeyboard();

        // Set up Activity Monitor
        // keeps track every time dashboard activity has been called
        Instrumentation.ActivityMonitor dashboardActivityMonitor =
                mInstrumentation.addMonitor(DashboardActivity.class.getName(),
                        null, false);

        // Click login button
        TouchUtils.clickView(this, mActivityRule.findViewById(R.id.email_sign_in_button));

        // Wait for the dashboard activity to Load within 10000 milliseconds
        DashboardActivity receiverActivity = (DashboardActivity)
                dashboardActivityMonitor.waitForActivityWithTimeout(10000);

        // Check that a new activity was created
        assertNotNull("Dashboard is null", receiverActivity);

        // Check the Dashboard activity has loaded
        assertEquals("Dashboard Activity has not been called",
                1, dashboardActivityMonitor.getHits());

        // Remove the Activity Monitor
        getInstrumentation().removeMonitor(dashboardActivityMonitor);

        database = mActivityRule.getMydb();
        database.deleteUser(database.getId(REG_USER));
    }
}
