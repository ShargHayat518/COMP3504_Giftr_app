package com.example.giftr_app;

import java.io.Serializable;

/**
 * User object class. stores email/userId, password, and authenticated status
 */
public class User implements Serializable {

    String email;
    String password;
    String authenticated = "false";

    /**
     * constructor method - creates user object
     * @param email email/userId (they are the same)
     */
    public User (String email) {
        this.email = email;
    }

    /**
     * constructor method - creates user object
     * @param email email/userId
     * @param password password
     */
    public User (String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * setter method - sets password
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getter method - gets password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * getter method - id/email
     * @return userId/email
     */
    public String getUserId() {
        return email;
    }

    /**
     * setter method - sets authenticated to true
     */
    public void setAuthenticated() {
        authenticated = "true";
    }

    /**
     * Checks if email is confirmed. if it is, user can't go to confirm email page again
     * @return confirmed status.
     */
    public String isAuthenticated() {
        return authenticated;
    }


}
