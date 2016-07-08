package com.datapp.buzz_movieselector.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.os.StrictMode;

import com.datapp.buzz_movieselector.model.JSONParser;
import com.datapp.buzz_movieselector.model.Movie;
import com.datapp.buzz_movieselector.model.MovieArrayAdapter;

import com.datapp.buzz_movieselector.model.User;

import android.support.v4.app.ListFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TopRentalsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TopRentalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopRentalsFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private JSONParser parser;
    private User currentUser;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mTextView;
    private OnFragmentInteractionListener mListener;

    public TopRentalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewReleasesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopRentalsFragment newInstance(String param1, String param2) {
        TopRentalsFragment fragment = new TopRentalsFragment();
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
        parser = new JSONParser(TopRentalsFragment.class.getSimpleName());
        currentUser = (User) getArguments().getSerializable("currentUser");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parser.makeRequest();
        MovieArrayAdapter adapter = new MovieArrayAdapter(getActivity(), parser.getMovies());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        Intent intent = new Intent(view.getContext(), MovieDetailActivity.class);
        Movie current = (Movie) list.getItemAtPosition(position);
        intent.putExtra("currentMovie", current);
        intent.putExtra("currentUser", this.currentUser);
        intent.putExtra("currentMajor", "Overall");
        startActivity(intent);
    }

/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/

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
        void onFragmentInteraction(Uri uri);
    }
}
