package com.datapp.buzz_movieselector.model;
import android.database.Cursor;

import java.io.Serializable;
/**
 * Created by taylor on 2/23/2016.
 * Class that contains the components for a user rating.
 */
public class Rating implements Serializable {

    private User user;
    private String comment;
    private int rating;
    private Movie movie;

    /**
     * Creates new rating.
     * @param user creating the rating.
     * @param comment made by the user.
     * @param rating made by the user.
     * @param movie being rated.
     */
    public Rating(User user, String comment, int rating, Movie movie) {
        this.user = user;
        this.comment = comment;
        this.rating = rating;
        this.movie = movie;
    }
    public Rating(Cursor cursor) {
        if (cursor != null) {
            //int id = cursor.getInt(cursor.getColumnIndex(RatingDatabaseHelper.COLUMN_ID));
            this.comment = cursor.getString(cursor.getColumnIndex(RatingDatabaseHelper.COLUMN_COMMENT));
            this.rating = cursor.getInt(cursor.getColumnIndex(RatingDatabaseHelper.COLUMN_RATING));
        }
    }
    /**
     * Gets the user.
     * @return user connected to the rating.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user to the one passes in.
     * @param user being connected to rating.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the comment.
     * @return comment connected to the rating.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment to the one passed in.
     * @param comment being connected to the rating.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the number rating.
     * @return rating being connected to overall rating.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the rating to the one passed in.
     * @param rating being connected to overall rating.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Gets the movie.
     * @return the movie that is being rated.
     */
    public Movie getMovie() { return movie; }

    /**
     * Sets the movie to the one passed in.
     * @param movie that is being rated.
     */
    public void setMovie(Movie movie) {this.movie = movie; }

    @Override
    public String toString(){
        return this.comment;
    }
}
