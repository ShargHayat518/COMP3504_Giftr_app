package com.example.giftr_app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Friend object class. Stores friend attributes name, dob, gender, interests, gift price range, and colour
 */
public class Friend implements Comparable<Friend>, Serializable {

    String name;
    int bYear;
    int bMonth;
    int bDay;
    String gender;
    String stringInterests;
    ArrayList <String> interests;
    float minPrice;
    float maxPrice;
    long countdown;
    long hours;
    int colour;
    String userId;
    String id;
    int imageId;



    /**
     * Constructor method. Sets colour to 0 (using it as null in this case), and sets min and max price to a reasonable default.
     */
    public Friend(){
        interests = new ArrayList();
        this.minPrice = 5;
        this.maxPrice = 50;
        gender = "other";
    }

    /**
     * Constructor method. Sets colour to 0 (using it as null in this case), and sets min and max price to a reasonable default.
     * @param name name of friend
     * @param bYear year of birth
     * @param bMonth month of birth
     * @param bDay day of birth
     */
    public Friend (String name, int bYear, int bMonth, int bDay, String userId) {
        this.name = name;
        this.bYear = bYear;
        this.bMonth = bMonth;
        this.bDay = bDay;
        interests = new ArrayList();
        this.minPrice = 5;
        this.maxPrice = 50;
        gender = "other";
    }

    /**
     * getter method - name
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * getter method - birth year
     * @return year
     */
    public int getbYear() {
        return bYear;
    }

    /**
     * getter method - birth month
     * @return month
     */
    public int getbMonth() {
        return bMonth;
    }

    /**
     * getter method - birth day of month
     * @return day
     */
    public int getbDay() {
        return bDay;
    }

    /**
     * getter method - user Id
     * @return user id associated with friend.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * setter method - user id associated with friend
     * @param userId user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * getter method - friend id
     * @return friend id
     */
    public String getId() {
        return id;
    }

    /**
     * setter method - friend id
     * @param id friend id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method - gender
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * getter method - colour
     * @return friend's chosen colour
     */
    public int getColour(){
        return colour;
    }

    /**
     * getter method - min price for gift price range
     * @return min price
     */
    public float getMinPrice() {
        return minPrice;
    }

    /**
     * getter method - max price for gift price range
     * @return max price
     */
    public float getMaxPrice() {
        return maxPrice;
    }

    /**
     * setter method - name
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter method - birth year
     * @param bYear year
     */
    public void setbYear(int bYear) {
        this.bYear = bYear;
    }

    /**
     * setter method - birth month
     * @param bMonth month
     */
    public void setbMonth(int bMonth) {
        this.bMonth= bMonth;
    }

    /**
     * setter method - birth day of month
     * @param bDay day
     */
    public void setbDay(int bDay) {
        this.bDay = bDay;
    }

    /**
     * setter method - gender
     * @param gender gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * setter method - price range
     * @param minPrice minimum price in range
     * @param maxPrice maximum price in range
     */
    public void setPriceRange(float minPrice, float maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    /**
     * setter method - arraylist of string interests
     * @param interests interests arraylist
     */
    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    /**
     * getter method - arraylist of interests
     * @return interests
     */
    public ArrayList<String> getInterests() {
        return interests;
    }

    /**
     * setter method - sets string interests (long string of all interests)
     * @param stringInterests interests
     */
    public void setStringInterests(String stringInterests) {
        this.stringInterests = stringInterests;
    }

    /**
     * getter method - returns string of interests
     * @return interests
     */
    public String getStringInterests() {
        return stringInterests;
    }

    /**
     * setter method - colour
     * @param colour colour
     */
    public void setColour(int colour){
        this.colour = colour;
    }

    /**
     * adds an interest to the arrayList of interests
     * @param interest
     */
    public void addInterest(String interest) {
        interests.add(interest);
    }

    /**
     * sets the countdown till birthday
     * @param countdown countdown in days
     */
    public void setCountdown(long countdown){
        this.countdown = countdown;
    }

    /**
     * getter method - get countdown
     * @return countdown
     */
    public long getCountdown(){
        return countdown;
    }

    /**
     * setter method - sets hour difference between long hours
     * @param hours difference
     */
    public void setHourDiff(long hours){
        this.hours = hours;
    }

    /**
     * getter method - returns hour difference
     * @return hours
     */
    public long getHourDiff(){
        return hours;
    }

    /**
     * compare countdowns
     * @param o
     * @return comparison
     */
    @Override
    public int compareTo(Friend o) {
        // TODO Auto-generated method stub
        return Long.compare(this.getCountdown(), o.getCountdown());
    }

    /**
     * toString
     * @return name
     */
    @Override
    public String toString(){

        return name;

    }

    /**
     * getter method - image id for icon
     * @return image id
     */
    public int getImageId(){
        return imageId;
    }

    /**
     * setter method - image id for icon
     * @param imageId int id
     * @return image id
     */
    public int setImageId(int imageId){
        return this.imageId = imageId;
    }




}

