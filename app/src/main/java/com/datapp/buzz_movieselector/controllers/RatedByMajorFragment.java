package com.datapp.buzz_movieselector.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.support.v4.app.ListFragment;
import android.widget.Button;


import com.datapp.buzz_movieselector.R;
import com.datapp.buzz_movieselector.model.JSONParser;
import com.datapp.buzz_movieselector.model.Movie;
import com.datapp.buzz_movieselector.model.MovieArrayAdapter;
import com.datapp.buzz_movieselector.model.Pair;
import com.datapp.buzz_movieselector.model.RatingTable;
import com.datapp.buzz_movieselector.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RatedByMajorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RatedByMajorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RatedByMajorFragment extends ListFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String major;
    private OnFragmentInteractionListener mListener;
    private Spinner spinner;
    private RatingTable ratingDB;
    private JSONParser parser;
    private User currentUser;

    public RatedByMajorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RatedByMajorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RatedByMajorFragment newInstance(String param1, String param2) {
        RatedByMajorFragment fragment = new RatedByMajorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        currentUser = (User) getArguments().getSerializable("currentUser");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        LayoutInflater inflater1 = inflater;
//        ViewGroup container1 = container;
//        Bundle savedInstanceState1 = savedInstanceState;

        View rootView = inflater.inflate(R.layout.fragment_rated_by_major, container, false);
        Button search = (Button) rootView.findViewById(R.id.search_major_button);
        search.setOnClickListener(this);

        this.spinner = (Spinner) rootView.findViewById(R.id.userMajorinSearch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.majors_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        Intent intent = new Intent(view.getContext(), MovieDetailActivity.class);
        Movie current = (Movie) list.getItemAtPosition(position);
        intent.putExtra("currentMovie", current);
        intent.putExtra("currentUser", currentUser);
        intent.putExtra("currentMajor", major);
        startActivity(intent);
    }

    public void onClick (View v) {
        this.major = this.spinner.getSelectedItem().toString();
        Log.d("INPUT", major);
        ratingDB = new RatingTable(getContext());
        ratingDB.open();
        parser = new JSONParser();
        populate();
    }

    private void populate() {
        ArrayList<Pair> moviePairs = ratingDB.getMoviesMajor(major);

        for (Pair<String, Double> p: moviePairs) {
            System.out.println("AN ID " + p.getLeft());
            String id = p.getLeft();
            double overallRating = p.getRight();
            parser.makeRequest(id, overallRating);
        }

        List<Movie> movies = parser.getMovies();

        //parser.clearMovies();
        MovieArrayAdapter adapter = new MovieArrayAdapter(getActivity(), movies);
        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
}
}
