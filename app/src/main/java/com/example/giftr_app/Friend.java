//package com.example.giftr_app;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
//public class Friend implements Comparable<Friend> {
//
//    String name;
//    int bYear;
//    int bMonth;
//    int bDay;
//    String gender;
//    ArrayList <String> interests;
//    int minPrice;
//    int maxPrice;
//    long countdown;
//    long hours;
//    int imageId;
//
//
//    public Friend(){
//        interests = new ArrayList();
//    }
//
//    public Friend (String name, int bYear, int bMonth, int bDay, String gender) {
//        this.name = name;
//        this.bYear = bYear;
//        this.bMonth = bMonth;
//        this.bDay = bDay;
//        this.gender = gender;
//        interests = new ArrayList();
//    }
//
//    public String getName(){
//        return name;
//    }
//
//    public int getbYear() {
//        return bYear;
//    }
//    public int getbMonth() {
//        return bMonth;
//    }
//    public int getbDay() {
//        return bDay;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//    public void setbYear(int bYear) {
//        this.bYear = bYear;
//    }
//    public void setbMonth(int bMonth) {
//        this.bMonth= bMonth;
//    }
//    public void setbDay(int bDay) {
//        this.bDay = bDay;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public void setPriceRange(int minPrice, int maxPrice) {
//        this.minPrice = minPrice;
//        this.maxPrice = maxPrice;
//    }
//
//    public void addInterest(String interest) {
//        interests.add(interest);
//    }
//    public void setCountdown(long countdown){
//        this.countdown = countdown;
//    }
//    public long getCountdown(){
//        return countdown;
//    }
//    public void setHourDiff(long hours){
//        this.hours = hours;
//    }
//    public long getHourDiff(){
//        return hours;
//    }
//
//    @Override
//    public int compareTo(Friend o) {
//        // TODO Auto-generated method stub
//        return Long.compare(this.getCountdown(), o.getCountdown());
//    }
//
//    @Override
//    public String toString(){
//
//        return name;
//
//    }
//
//    public int getImageId(){
//        return imageId;
//    }
//
//
//    public int setImageId(int imageId){
//        return this.imageId = imageId;
//    }
//
//
//}
//
package com.example.giftr_app;

import java.io.Serializable;
import java.util.ArrayList;

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
        this.minPrice = 0;
        this.maxPrice = 50;
    }

    /**
     * Constructor method. Sets colour to 0 (using it as null in this case), and sets min and max price to a reasonable default.
     * @param name
     * @param bYear
     * @param bMonth
     * @param bDay
     */
    public Friend (String name, int bYear, int bMonth, int bDay, String userId) {
        this.name = name;
        this.bYear = bYear;
        this.bMonth = bMonth;
        this.bDay = bDay;
        interests = new ArrayList();
        this.minPrice = 0;
        this.maxPrice = 50;
    }

    public String getName(){
        return name;
    }

    public int getbYear() {
        return bYear;
    }
    public int getbMonth() {
        return bMonth;
    }
    public int getbDay() {
        return bDay;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public int getColour(){
        return colour;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setbYear(int bYear) {
        this.bYear = bYear;
    }
    public void setbMonth(int bMonth) {
        this.bMonth= bMonth;
    }
    public void setbDay(int bDay) {
        this.bDay = bDay;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPriceRange(float minPrice, float maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setStringInterests(String stringInterests) {
        this.stringInterests = stringInterests;
    }

    public String getStringInterests() {
        return stringInterests;
    }

    //    public String getStringInterests() {
//        StringBuilder builder;
//        String stringInterests;
//
//        for (int i = 0; i < interests.size(); i++) {
//
//            if (interests.get(i + 1).toString() == null){
//                break;
//            }
//            builder = builder.append(i + ", ");
//        }
//        stringInterests = builder.toString();
//
//        return stringInterests;
//    }

    public void setColour(int colour){
        this.colour = colour;
    }

    public void addInterest(String interest) {
        interests.add(interest);
    }
    public void setCountdown(long countdown){
        this.countdown = countdown;
    }
    public long getCountdown(){
        return countdown;
    }
    public void setHourDiff(long hours){
        this.hours = hours;
    }
    public long getHourDiff(){
        return hours;
    }

    @Override
    public int compareTo(Friend o) {
        // TODO Auto-generated method stub
        return Long.compare(this.getCountdown(), o.getCountdown());
    }

    @Override
    public String toString(){
        return name;
    }

    public int getImageId(){
        return imageId;
    }


    public int setImageId(int imageId){
        return this.imageId = imageId;
    }


}

