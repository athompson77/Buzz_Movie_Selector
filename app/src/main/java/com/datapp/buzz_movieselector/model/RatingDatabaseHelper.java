package com.datapp.buzz_movieselector.model;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RatingDatabaseHelper extends SQLiteOpenHelper {
    // Database table
    private static final String DATABASE_NAME = "RatingsDB.db";
    public static final String TABLE_RATINGS = "Ratings";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_MOVIE = "Movie";
    public static final String COLUMN_USER = "User";
    public static final String COLUMN_COMMENT = "Comment";
    public static final String COLUMN_RATING = "Rating";
    public static final String COLUMN_MAJOR = "Major";
    private static final int DATABASE_VERSION = 1;
    // Database creation SQL statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_RATINGS
            + " ("
            + COLUMN_ID + " INTEGER primary key autoincrement, "
            + COLUMN_MOVIE + " TEXT not NULL, " //Movie _id in Rotten Tomatoes
            + COLUMN_USER + " INTEGER, " //User _id primary key in UsersDB.db
            + COLUMN_COMMENT + " TEXT not NULL, "
            + COLUMN_RATING + " INTEGER, " // 1 through 5
            + COLUMN_MAJOR + " TEXT not NULL "
            + ");";

    public RatingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        onCreate(db);
    }
}