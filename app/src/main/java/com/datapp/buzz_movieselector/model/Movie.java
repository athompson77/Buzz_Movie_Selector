package com.datapp.buzz_movieselector.model;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.io.Serializable;
/**
 * Created by taylor on 2/23/2016.
 * Class that contains components for a movie.
 */
public class Movie implements Serializable {
    private String id;
    private String name;
    private int year;
    private String image;
    private ArrayList<Rating> rating;
    private double overallRating;

    public Movie() {

    }

    /**
     * Creates a movie with user ratings.
     * @param id string id of the movie.
     * @param name string name of the movie.
     * @param year string year the movie was made.
     * @param image string image of the movie poster.
     * @param rating arraylist of ratings.
     */
    public Movie(String id, String name, int year, String image,
                 ArrayList<Rating> rating) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.image = image;
        this.rating = rating;
        this.overallRating = 0;
    }

    /**
     * Gets the id.
     * @return id that uniquely identifies the movie.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id to the one passed in.
     * @param id that uniquely identifies the movie.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name.
     * @return name of the movie.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name to the one passed in.
     * @param name of the movie.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the year.
     * @return year the movie came out.
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year.
     * @param year the movie came out.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the image.
     * @return image of the movie poster.
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image.
     * @param image of the movie poster.
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Gets the rating.
     * @return rating of the movie.
     */
    public ArrayList<Rating> getRating() {
        return rating;
    }

    /**
     * Sets the rating.
     * @param rating of the movie.
     */
    public void setRating(ArrayList<Rating> rating) {
        this.rating = rating;
    }

    public double getOverallRating() { return this.overallRating; }

    public void setOverallRating(double overallRating) { this.overallRating = overallRating; }

    @Override
    public String toString() { return this.name; }

    /**
     * Loads an image from a source url as a BitMap. Returns true if image was successfully loaded,
     * false otherwise
     * @param fileUrl source url to get image
     * @param iv ImageView where image will be shown
     */
    public boolean loadImageFromURL(String fileUrl, ImageView iv){
        try {

            URL myFileUrl = new URL (fileUrl);
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            iv.setImageBitmap(BitmapFactory.decodeStream(is));

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
