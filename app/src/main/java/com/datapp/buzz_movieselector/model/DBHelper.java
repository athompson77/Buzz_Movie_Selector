package com.datapp.buzz_movieselector.model;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;

public class DBHelper {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public DBHelper(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }
    public void open() {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }
    /**
     * Insert a new user to the database.
     * @param username string representing user's username
     * @param password string representing user's password
     * @return true if successfully added, false if user already exists.
     */
    public boolean insertUser(String username, String password, String major) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.USERS_COLUMN_USERNAME, username);
        contentValues.put(MySQLiteHelper.USERS_COLUMN_PASSWORD, password);
        contentValues.put(MySQLiteHelper.USERS_COLUMN_MAJOR, major);
        if (availableUsername(username)) {
            database.insert(MySQLiteHelper.USERS_TABLE_NAME, null, contentValues);
            return true;
        } else {
            return false;
        }
    }
    /**
     *
     * @param username string representing the username.
     * @return return true if username is not on the database, false otherwise.
     */
    public boolean availableUsername (String username) {
        final Cursor cursor = database.rawQuery("SELECT " + MySQLiteHelper.USERS_COLUMN_ID + " FROM " + MySQLiteHelper.USERS_TABLE_NAME + " WHERE " + MySQLiteHelper.USERS_COLUMN_USERNAME + " = ?", new String[]{username});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return false;
        } else {
            return true;
        }
    }
    /**
     *
     * @param username string representing the username.
     * @param password string representing the password.
     * @return -1 if wrong username and password, 0 if wrong password, 1 if success
     */
    public int validateLogin (String username, String password) {
        int success = - 1;
        if (!availableUsername(username)) success++;
        Cursor cursor = database.rawQuery("SELECT id FROM " + MySQLiteHelper.USERS_TABLE_NAME + " WHERE " + MySQLiteHelper.USERS_COLUMN_USERNAME + " = ? and "+ MySQLiteHelper.USERS_COLUMN_PASSWORD + " = ?", new String[]{username, password});
        if (cursor != null && cursor.getCount() > 0) {
            success++;
            cursor.close();
        }
        return success;
    }

    /**
     * getMajor method will give us a cursor with the data associated with username.
     * @param username the username which we want to get data from
     * @return mCursor cursor of the database containing the info.
     * @throws SQLException if an error occurs while executing query and cursor is null.
     */
    public Cursor getMajor(String username) throws SQLException {
        Cursor mCursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.USERS_TABLE_NAME + " WHERE " + MySQLiteHelper.USERS_COLUMN_USERNAME + " = ? ;", new String[] { username});
        if (mCursor != null) {
            mCursor.moveToFirst();
        } else {
            throw new SQLException("Unable to get major field from Database for username" + username);
        }
        return mCursor;
    }

    /**
     * Method to get the status of a certain user.
     * @param username the username which we want to get status from
     * @return the status.
     */
    public String getStatus(String username) {
        String status = null;
        Cursor cursor = database.rawQuery("SELECT " + MySQLiteHelper.USERS_COLUMN_STATUS
               + " FROM " + MySQLiteHelper.USERS_TABLE_NAME + " WHERE " + MySQLiteHelper.USERS_COLUMN_USERNAME + " = ? ;", new String[]{username});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            status = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.USERS_COLUMN_STATUS));
            cursor.close();
        }
        return status;
    }
    /**
     * Method to verify if a certain user has status of BANNED
     * @param username the username which we want to get info from
     * @return true if banned, false otherwise.
     */
    public boolean isBanned(String username) {
        return getStatus(username).equals("BANNED");
    }
    /**
     * Method to verify if a certain user has status of ACTIVE.
     * @param username the username which we want to get info from
     * @return true if active, false otherwise.
     */
    public boolean isActive(String username) {
        return getStatus(username).equals("ACTIVE");
    }
    /**
     * Method to verify if a certain user has status of LOCKED.
     * @param username the username which we want to get info from
     * @return true if locked, false otherwise.
     */
    public boolean isLocked(String username) {
        return getStatus(username).equals("LOCKED");
    }
    /**
     * Method to verify if a certain user has status of ADMIN.
     * @param username the username which we want to get info from
     * @return true if admin, false otherwise.
     */
    public boolean isAdmin(String username) {
        return getStatus(username).equals("ADMIN");
    }
    /**
     * Method for setting a user's status to LOCKED in database.
     * @param username the username of the user to lock
     * @return true if a user was affected, false otherwise.
     */
    public boolean lockUser(String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.USERS_COLUMN_STATUS, "LOCKED");
        String whereClause = MySQLiteHelper.USERS_COLUMN_USERNAME +" = ? ";
        return database.update(MySQLiteHelper.USERS_TABLE_NAME, contentValues, whereClause , new String[] {username} ) != 0;
    }
    /**
     * Method for setting a user's status to BANNED in database
     * @param username the username of the user to ban
     * @return true if a user was affected, false otherwise.
     */
    public boolean banUser(String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.USERS_COLUMN_STATUS, "BANNED");
        String whereClause = MySQLiteHelper.USERS_COLUMN_USERNAME +" = ? ";
        return database.update(MySQLiteHelper.USERS_TABLE_NAME, contentValues, whereClause , new String[] {username} ) != 0;
    }
    /**
     * Method for setting a user's status to ACTIVE in database
     * @param username the username of the user to ban
     * @return true if a user was affected, false otherwise.
     */
    public boolean activateUser(String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.USERS_COLUMN_STATUS, "ACTIVE");
        String whereClause = MySQLiteHelper.USERS_COLUMN_USERNAME +" = ? ";
        return database.update(MySQLiteHelper.USERS_TABLE_NAME, contentValues, whereClause , new String[] {username} ) != 0;
    }

    /**
     * Get the id associated with the user in the Database.
     * @param username user's username.
     * @return an int of the id in DB.
     */
    public int getId(String username) {
        int id;
        Cursor cursor = database.rawQuery("SELECT id FROM " + MySQLiteHelper.USERS_TABLE_NAME + " WHERE " + MySQLiteHelper.USERS_COLUMN_USERNAME + " = ? ;", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
        } else {
            id = 0;
        }
        return id;
    }
    /**
     * getData method will give us a cursor with the data associated with id.
     * @param id the id which we want to get data from
     * @return cursor of the database containing the info.
     */
    public Cursor getData(int id) {
        return database.rawQuery( "SELECT * FROM " + MySQLiteHelper.USERS_TABLE_NAME + " WHERE id = ? ", new String[] { Integer.toString(id) });
    }

    /**
     * method to get rows in our database
     * @return an int representing number of rows in Database
     */
    public int numberOfRows() {
        return (int) DatabaseUtils.queryNumEntries(database, MySQLiteHelper.USERS_TABLE_NAME);
    }

    /**
     * method to update a user in Database
     * @param id id Integer representing user's id in database
     * @param username string representing new user's username
     * @param password string representing new user's password
     * @param major string representing new user's major
     * @param interests string representing new interests
     * @return true if successful
     */
    public boolean updateUser (Integer id, String username, String password, String major, String interests) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.USERS_COLUMN_USERNAME, username);
        contentValues.put(MySQLiteHelper.USERS_COLUMN_PASSWORD, password);
        contentValues.put(MySQLiteHelper.USERS_COLUMN_MAJOR, major);
        contentValues.put(MySQLiteHelper.USERS_COLUMN_INTEREST, interests);
        database.update(MySQLiteHelper.USERS_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    /**
     * method to delete a user by id from Database
     * @param id id Integer representing user's id in database
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise.
     */
    public Integer deleteUser (Integer id) {
        return database.delete(MySQLiteHelper.USERS_TABLE_NAME, "id = ? ", new String[] { Integer.toString(id) });
    }

    /**
     * getAllUsers method to get usernames in the Database.
     * @return array_list an ArrayList of all the usernames in Database.
     */
    public ArrayList<String> getAllUsers() {
        ArrayList<String> array_list = new ArrayList<>();
        Cursor res =  database.rawQuery( "SELECT * FROM " + MySQLiteHelper.USERS_TABLE_NAME, null );
        res.moveToFirst();
        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(MySQLiteHelper.USERS_COLUMN_USERNAME)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}