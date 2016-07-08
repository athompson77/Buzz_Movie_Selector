package com.datapp.buzz_movieselector.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v4.app.ListFragment;
import android.widget.Button;


import com.datapp.buzz_movieselector.R;
import com.datapp.buzz_movieselector.model.JSONParser;
import com.datapp.buzz_movieselector.model.Movie;
import com.datapp.buzz_movieselector.model.MovieArrayAdapter;
import com.datapp.buzz_movieselector.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends ListFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private EditText searchBox;
    private JSONParser parser;
    private User currentUser;
    private View view;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        parser = new JSONParser(SearchFragment.class.getSimpleName());
        currentUser = (User) getArguments().getSerializable("currentUser");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        LayoutInflater inflater1 = inflater;
//        ViewGroup container1 = container;
//        Bundle savedInstanceState1 = savedInstanceState;

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        this.view = rootView;
        this.searchBox = (EditText) rootView.findViewById(R.id.searchMovie);
        Button search = (Button) rootView.findViewById(R.id.search_button);
        search.setOnClickListener(this);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick (View v) {
        Log.d("INPUT", searchBox.getText().toString());
        boolean searchEmpty = TextUtils.isEmpty(this.searchBox.getText().toString());
        if (!searchEmpty) {
            Log.d("TEST", "IN IF");
            String searchWord = searchBox.getText().toString();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build());
            parser.makeRequest(searchWord);
            MovieArrayAdapter adapter = new MovieArrayAdapter(getActivity(), parser.getMovies());
            setListAdapter(adapter);
            hideKeyboard();
        } else {
            Log.d("TEST", "Box is empty");
        }
    }

    /**
     * Method to hide keyboard when search button is clicked
     *
     */
    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) view
                .getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = view.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder,
                InputMethodManager.HIDE_NOT_ALWAYS);
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
