package com.datapp.buzz_movieselector.controllers;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import android.webkit.WebView;
import android.widget.Toast;
import com.datapp.buzz_movieselector.R;
import com.datapp.buzz_movieselector.model.User;

import java.util.ArrayList;
import java.util.List;

import com.datapp.buzz_movieselector.controllers.SearchFragment;
import com.datapp.buzz_movieselector.controllers.RatedByMajorFragment;
import com.datapp.buzz_movieselector.controllers.NewReleasesFragment;
import com.datapp.buzz_movieselector.controllers.TopRentalsFragment;

/**
 * Created by alex on 2/5/16.
 *
 * Main screen of app when log in is successful. Will allow for movie search and top rentals
 */
public class DashboardActivity extends AppCompatActivity
    implements SearchFragment.OnFragmentInteractionListener, TopRentalsFragment.OnFragmentInteractionListener,
    RatedByMajorFragment.OnFragmentInteractionListener, NewReleasesFragment.OnFragmentInteractionListener {


    private WebView mWebView;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //PreferenceManager.setDefaultValues(this, R.xml.profile_preferences, false);
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");
        mWebView = new WebView(this);
        //this.context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        setupSectionPager(mViewPager);
        //mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        //now back button is disabled to avoid returning to login
        //fix to switch between fragments
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_profile:
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                profileIntent.putExtra("user", this.user);
                startActivity(profileIntent);
                finish();
                return true;
            case R.id.action_logout:
                Intent intent = new Intent(this, OpeningScreenActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_refresh:
                mWebView.reload();
                Toast.makeText(DashboardActivity.this, "Refresh App", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method that sets up the fragments and hooks them up the the viewpager adapter
     * Used in onCreate()
     * @param viewPager the viewpager object created by the default tab activity
     */
    private void setupSectionPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putSerializable("currentUser", user);
        NewReleasesFragment newReleasesFragment = new NewReleasesFragment();
        newReleasesFragment.setArguments(bundle);
        adapter.addFragment(newReleasesFragment, "New in Theaters");

        TopRentalsFragment topRentalsFragment = new TopRentalsFragment();
        topRentalsFragment.setArguments(bundle);
        adapter.addFragment(topRentalsFragment, "New DVD Releases");

        RatedByMajorFragment ratedByMajorFragment = new RatedByMajorFragment();
        ratedByMajorFragment.setArguments(bundle);
        adapter.addFragment(ratedByMajorFragment, "Top Movies");

        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        adapter.addFragment(searchFragment, "Search");
        
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //DO NOT REMOVE THIS PLEASE
    }

    /**
     * Method to get current user object in activity. Mainly for test purposes.
     * DO NOT REMOVE
     *
     * @return current user object
     */
    public User getCurrentUser() {
        return this.user;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            return mFragmentList.get(position);
        }

        /**
         * Method that allows the addition of a fragment and it's title in one call.
         * Used by setupSectionPager.
         * @param fragment the fragment wanted to add as a tab
         * @param title the title of the fragment in the tabs
         */
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
