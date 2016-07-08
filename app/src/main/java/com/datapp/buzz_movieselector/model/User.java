package com.datapp.buzz_movieselector.model;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by daniel on 2/2/16.
 *
 * User object that stores all currently logged in user's data
 */
public class User implements Serializable {

    private String name;
    private String password;
    private String major;
    private String interests;
    private String status;
    private int id;

    /**
     * Creates a new user object by using constructor chaining!
     *
     * @param name username of the user
     * @param password password of user
     * @param major major of user
     */
    public User(String name, String password, String major) {
        this(name, password, major,"Set your Interests!");
    }
    /**
     * Creates user when interests are set
     *
     * @param name username of the user
     * @param password password of user
     * @param major major of user
     * @param interests interests of user
     */
    public User(String name, String password, String major, String interests) {
        this.major = major;
        this.name = name;
        this.password = password;
        this.interests = interests;
        this.status = "ACTIVE";
    }

    /**
     * Creates user from data base
     *
     * @param cursor cursor where the data base is currently pointing
     */
    public User(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            int id =  Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String name =  cursor.getString(cursor.getColumnIndex("username"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String major = cursor.getString(cursor.getColumnIndex("major"));
            String interest = cursor.getString(cursor.getColumnIndex("interest"));
            String status = cursor.getString(cursor.getColumnIndex("status"));
            this.id = id;
            this.name = name;
            this.password = password;
            this.major = major;
            this.interests =interest;
            this.status = status;
        }
    }

    /**
     * Gets the status of a user
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of a user
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Bans a user
     */
    public void banUser() {
        this.status = "BANNED";
    }

    /**
     * Locks a user
     */
    public void lockUser() {
        this.status = "LOCKED";
    }

    /**
     * Gets banned status of user
     *
     * @return true if banned and false otherwise
     */
    public boolean isBanned() {
        return this.status.equals("BANNED");
    }

    /**
     * Gets locked status of user
     *
     * @return true if locked and false otherwise
     */
    public boolean isLocked() {
        return this.status.equals("LOCKED");
    }

    /**
     * Reactivates user
     */
    public void reactivate() {
        this.status = "ACTIVE";
    }


    /**
     * Gets the major for the user
     */
    public String getMajor() {
        return major;
    }

    /**
     * Sets the major for the user
     *
     * @param major the new major to be set
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * Get the id for the user
     *
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the id for the user
     *
     * @param id the new id to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the Name for the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for the user
     *
     * @param name sets the new name for the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the password for the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the interests for the user
     */
    public String getInterests() {
        return interests;
    }

    /**
     * Sets the interests of the user to the new string
     *
     * @param newI the new interests to be set for the user
     */
    public void setInterests(String newI) {
        this.interests = newI;
    }
}
