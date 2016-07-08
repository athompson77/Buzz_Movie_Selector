package com.datapp.buzz_movieselector.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class MySQLiteHelper extends SQLiteOpenHelper {
    /**
     * Information for UsersDB.db
     */
    private static final String DATABASE_NAME = "UsersDB.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_USERNAME = "username";
    public static final String USERS_COLUMN_PASSWORD = "password";
    public static final String USERS_COLUMN_MAJOR = "major";
    public static final String USERS_COLUMN_INTEREST = "interest";
    public static final String USERS_COLUMN_STATUS = "status";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + USERS_TABLE_NAME + " ( "
            + USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USERS_COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, "
            + USERS_COLUMN_PASSWORD + " TEXT NOT NULL, "
            + USERS_COLUMN_MAJOR + " TEXT NOT NULL, "
            + USERS_COLUMN_INTEREST + " TEXT DEFAULT 'N/A', "
            + USERS_COLUMN_STATUS + " TEXT DEFAULT 'ACTIVE' );";

    private static final String DATABASE_CREATE_ADMIN = String.format(
            "INSERT INTO %s ( %s, %s, %s, %s, %s )"
            + " VALUES (0, 'ADMIN', 'DATAPP', 'N/A', 'ADMIN');",
            USERS_TABLE_NAME, USERS_COLUMN_ID, USERS_COLUMN_USERNAME, USERS_COLUMN_PASSWORD,
            USERS_COLUMN_MAJOR, USERS_COLUMN_STATUS);
    /**
     * Constructor
     * @param context Application Context where DBHelper is created.
     */
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE_ADMIN);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // simple database upgrade operation:
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME); // 1) drop the old table
        onCreate(db); // 2) create a new database
    }
}
