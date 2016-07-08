package com.datapp.buzz_movieselector.model;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RatingTable {
    private SQLiteDatabase database;
    private RatingDatabaseHelper dbHelper;

    public RatingTable(Context context) {
        dbHelper = new RatingDatabaseHelper(context);
    }

    /**
     * open connection to Rating Table
     */
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * close connection to Rating Table
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Method to insert a rating to database.
     * @param movie Movie being are rated
     * @param user User that rated the movie
     * @param comment comment made on the rating
     * @param rating rating value [0-5]
     * @return true or false based on successful addition to DB
     */
    public boolean insertRating (Movie movie, User user, String comment, int rating) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RatingDatabaseHelper.COLUMN_USER, user.getId());
        contentValues.put(RatingDatabaseHelper.COLUMN_MOVIE, movie.getId());
        contentValues.put(RatingDatabaseHelper.COLUMN_COMMENT, comment);
        contentValues.put(RatingDatabaseHelper.COLUMN_RATING, rating);
        contentValues.put(RatingDatabaseHelper.COLUMN_MAJOR, user.getMajor());
        long success = database.insert(RatingDatabaseHelper.TABLE_RATINGS, null, contentValues);
        return (success != -1);
    }

    /**
     * Get all ratings associated to a specific Rotten Tomatoes movie
     * @param movieId unique Rotten Tomatoes movie id
     * @return android.database.Cursor cursor with all ratings made for movie
     */
    public Cursor getRatingsbyMovie(String movieId, String major) {
        if (major.equals("Overall")) {
            return database.rawQuery("SELECT * FROM " + RatingDatabaseHelper.TABLE_RATINGS
                    + " WHERE " + RatingDatabaseHelper.COLUMN_MOVIE + " = ? ;", new String[]{movieId});
        } else {
            return database.rawQuery("SELECT * FROM " + RatingDatabaseHelper.TABLE_RATINGS
                    + " WHERE " + RatingDatabaseHelper.COLUMN_MOVIE + " = ? AND "+ RatingDatabaseHelper.COLUMN_MAJOR+" = ? ;", new String[]{movieId, major});
        }
    }

    /**
     * Get all ratings associated to a specific User
     * @param userID unique User id found in UsersDB.db (primary key)
     * @return android.database.Cursor cursor with all ratings made by user
     */
    public Cursor getRatingsbyUser(int userID) {
        return database.rawQuery("SELECT * FROM " + RatingDatabaseHelper.TABLE_RATINGS
                + " WHERE " + RatingDatabaseHelper.COLUMN_USER + " = ? ;", new String[]{Integer.toString(userID)});
    }

    /**
     * Get the average rating of a movie truncated to 2 decimal places
     * @param movieID the Rotten Tomatoes id of the movie
     * @return rating of the movie.
     */
    private double getAvgRating(String movieID) {
        double avg = 0.0;
        Cursor cursor = database.rawQuery("SELECT "+ RatingDatabaseHelper.COLUMN_RATING +" FROM " + RatingDatabaseHelper.TABLE_RATINGS
                + " WHERE " + RatingDatabaseHelper.COLUMN_MOVIE + " = ? ;", new String[]{movieID});
        if (cursor == null || cursor.getCount() == 0) return 0;
        int count = cursor.getCount();
        while(cursor.moveToNext()) {
            int rating = cursor.getInt(cursor.getColumnIndex(RatingDatabaseHelper.COLUMN_RATING));
            avg += rating;
        }
        cursor.close();
        return Math.round((avg/count)*100.0)/100.0;
    }

    /**
     * Get all the movies ordered by rating (best to worst) related to a specific major
     * @param major major to filter
     * @return an ArrayList of Pair[ String rotten tomatoes movieID | Integer avgRating ]
     */
    public ArrayList<Pair> getMoviesMajor(String major) {
        Set<String> movieIDs = new HashSet<>();
        ArrayList<Pair> movies = new ArrayList<>();
        Cursor cursor;
        if(major.equals("Overall")) {
            cursor = database.rawQuery("SELECT "+ RatingDatabaseHelper.COLUMN_MOVIE +" FROM " + RatingDatabaseHelper.TABLE_RATINGS, null);
        } else {
            cursor = database.rawQuery("SELECT " + RatingDatabaseHelper.COLUMN_MOVIE + " FROM " + RatingDatabaseHelper.TABLE_RATINGS
                    + " WHERE " + RatingDatabaseHelper.COLUMN_MAJOR + " = ? ;", new String[]{major});
        }
        while(cursor != null && cursor.moveToNext()) {
            String movieID = cursor.getString(cursor.getColumnIndex(RatingDatabaseHelper.COLUMN_MOVIE));
            movieIDs.add(movieID);
        }
        for (String movie: movieIDs) {
            if (major.equals("Overall")) {
                movies.add(new Pair<>(movie, getAvgRating(movie)));
            } else {
                movies.add(new Pair<>(movie, getAvgRatingbyMajor(movie, major)));
            }
        }
        if (cursor != null) cursor.close();
        Collections.sort(movies);
        return movies;
    }

    /**
     * Method to get the average rating for a specific movie made just by users with a specific major
     * @param movieID rotten tomatoes movie id to get rating
     * @param major major to filter
     * @return a double rounded to two decimal places of the average rating
     */
    public double getAvgRatingbyMajor(String movieID, String major) {
        if (major.equals("Overall")) return getAvgRating(movieID);
        double avg = 0.0;
        Cursor cursor = database.rawQuery("SELECT "+ RatingDatabaseHelper.COLUMN_RATING +" FROM " + RatingDatabaseHelper.TABLE_RATINGS
                + " WHERE " + RatingDatabaseHelper.COLUMN_MOVIE + " = ? AND " + RatingDatabaseHelper.COLUMN_MAJOR +" = ? ;", new String[]{movieID, major});
        if (cursor == null || cursor.getCount() == 0) return 0;
        int count = cursor.getCount();
        while(cursor.moveToNext()) {
            int rating = cursor.getInt(cursor.getColumnIndex(RatingDatabaseHelper.COLUMN_RATING));
            avg += rating;
        }
        cursor.close();
        return Math.round((avg/count)*100.0)/100.0;
    }
}
