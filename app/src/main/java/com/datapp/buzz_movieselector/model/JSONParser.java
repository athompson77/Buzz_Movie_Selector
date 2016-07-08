package com.datapp.buzz_movieselector.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class JSONParser {
    private List<Movie> movies;
    private JSONObject json;
    private String activity;
    private static final String API_KEY = "yedukp76ffytfuy24zsqk7f5";
    private static final String ROTTEN = "http://api.rottentomatoes.com/api/public/v1.0/";
    private static final String NEW_RELEASES_URL = ROTTEN + "lists/movies/in_theaters.json?apikey=" + API_KEY;
    private static final String NEW_DVDS_URL = ROTTEN + "lists/dvds/new_releases.json?apikey=" + API_KEY + "&page_limit=50";
    private static final String SEARCH_WITH_ID = ROTTEN + "movies/";
    private static final String SEARCH_URL = ROTTEN + "movies.json?apikey=" + API_KEY;


    /**
     * Creates a JSONParser object.
     */
    public JSONParser() {
        this.movies = new ArrayList<>();
    }

    /**
     * Creates a JSONParser object.
     * @param activity string activity where object is created.
     */
    public JSONParser(String activity) {
        this.activity = activity;
        movies = new ArrayList<>();
    }

    /**
     * Makes http request.
     */
    public void makeRequest() {
        switch (activity) {
            case "NewReleasesFragment":
                json = new RequestHandler().doInBackground(NEW_RELEASES_URL);
                break;
            case "TopRentalsFragment":
                json = new RequestHandler().doInBackground(NEW_DVDS_URL);
                break;
        }
        parseJSON(json);
    }

    /**
     * Makes http request.
     * @param searchFor key to search for in http request
     */
    public void makeRequest(String searchFor) {
        searchFor = searchFor.replaceAll(" ", "+");
        json = new RequestHandler().doInBackground(SEARCH_URL + "&q="+ searchFor + "&page_limit=15");
        parseJSON(json);
    }

    /**
     * Makes http request.
     * @param id id of movie that will be searched
     * @param overallRating rating to give to movie (from rating's database)
     */
    public void makeRequest(String id, double overallRating) {
        json = new RequestHandler().doInBackground(SEARCH_WITH_ID + id + ".json?apikey=" + API_KEY);
        System.out.println("JSON: " + json);
        parseJSON(json, overallRating);
    }

    /**
     * Returns List of Movie objects obtained in http request
     */
    public List<Movie> getMovies() {
        return this.movies;
    }

    /**
     * Parses JSONObject to create Movie objects and populate movies List
     * @param json data to parse
     */
    private void parseJSON(JSONObject json) {
        this.json = json;
        if (!movies.isEmpty()) {
            movies.clear();
        }
        //PARSING
        if (json != null) {
            int total = json.optInt("total");
            JSONArray jsa = json.optJSONArray("movies");
            for (int i = 0; i < total; i++) {
                JSONObject jso = jsa.optJSONObject(i);
                if (jso != null) {
                    String title = jso.optString("title");
                    String id = jso.optString("id");
                    int year = jso.optInt("year");
                    JSONObject posters = jso.optJSONObject("posters");
                    String image = posters.optString("thumbnail");
                    Movie movie = new Movie();
                    movie.setName(title);
                    movie.setId(id);
                    movie.setYear(year);
                    movie.setImage(image);
                    movies.add(movie);
                }
            }
        }
    }

    /**
     * Parses JSONObject to create Movie objects and populate movies List
     * Sets movie's overall rating to the overallRating parameter
     * @param json data to parse
     */
    private void parseJSON(JSONObject json, double overallRating) {
        //this.json = json;
        //PARSING
        if (json != null) {
            String title = json.optString("title");
            String id = json.optString("id");
            int year = json.optInt("year");
            JSONObject posters = json.optJSONObject("posters");
            String image = posters.optString("thumbnail");
            Movie movie = new Movie();
            movie.setName(title);
            movie.setId(id);
            movie.setYear(year);
            movie.setImage(image);
            movie.setOverallRating(overallRating);
            movies.add(movie);
        }
    }

    public void clearMovies() {
        movies.clear();
    }
}
