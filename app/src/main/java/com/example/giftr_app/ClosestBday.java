package com.example.giftr_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;


public class ClosestBday extends AppCompatActivity implements View.OnClickListener{


    public LinkedList<Friend> friendSLL = new LinkedList<>();
    public LinkedList<Friend> closestBdaySLL = new LinkedList<Friend>();

    String gender = "other";



    SimpleDateFormat sdfOld = new SimpleDateFormat("yyyyMMdd");
    ListView listView;
    RequestQueue requestQueue;
    Date tDate = new Date();
    LocalDate currDate = tDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    int[] imgId = {R.drawable.grandmother_portraits,R.drawable.stock2,R.drawable.trump};

    Friend friend;
    String userId;

    TextView image;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        findViewById(R.id.addFriend).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.birthdays).setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(this);
//        image = findViewById(R.id.profilePic);
//
//
//        image.setBackgroundColor(friend.getColour());

        getClosestBdays();


    }

    /**
     * Unpack the bundle that it receives, checks whether a specific user has friends.
     * Iterates through the database to look for friends. Adds necessary friends to a linked list,
     * if user has no friends, the page will go to another class
     */
    private void getClosestBdays() {

        //receiving userId bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //unpacck bundle if not null
        if (bundle != null) {

            userId = (String) bundle.getSerializable("userId");


            String baseURl = "https://pcflbmodxf.execute-api.ca-central-1.amazonaws.com/crudStage/friend_item";
            String url = baseURl;


            JsonObjectRequest jRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // What should we do after the response is ready

                            try {


                                JSONArray jArray = response.getJSONArray("Items");

                            if(jArray.length() > 0){

                                for(int i =0; i<jArray.length();i++){


                                    JSONObject obj = jArray.getJSONObject(i);

                                    String name = obj.getString("name");
                                    int bYear = obj.getInt("birth_year");
                                    int bMonth = obj.getInt("birth_month");
                                    int bDay = obj.getInt("birth_day");
                                    gender = obj.getString("gender");
                                    String friendsUserId = obj.getString("user_id");
                                    int colour = obj.getInt("color_id");
                                    String interests = obj.getString("interests");
                                    int min = obj.getInt("min_price");
                                    int max = obj.getInt("max_price");


                                    //checks to see if the userId extracted from bundle has the same userId
                                    //as any of the friends, if so, create a friend obj and add to friend
                                    //list
                                    if(userId.equals(friendsUserId)){

                                        friend= new Friend(name,bYear, bMonth, bDay, gender);
                                        friend.setColour(colour);
                                        friend.setStringInterests(interests);
                                        friend.setPriceRange(min,max);


                                         friendSLL.add(friend);


                                        Integer birthInfo = concat( currDate.getYear(), friend.getbMonth(), friend.getbDay());
                                        addInformation(friend,birthInfo);

                                    }

                                }
                                setListView(friendSLL);
                            }
                            //iterates through all the friends
                            else{
                                Intent intent = new Intent(ClosestBday.this, NoFriendsYet.class);

                                Bundle info = new Bundle();

                                //putting email in bundle
                                info.putSerializable("userId", userId);
                                intent.putExtras(info);
                                startActivity(intent);
                            }

                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // What should we do if an error happens
                            Toast.makeText(ClosestBday.this, "Some thing went wrong: "+ error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            );

            requestQueue.add(jRequest);
        }

    }

    /**
     * This takes the input from friend's bday and concatenate it
     *
     * for example: 20210210 from byear 2021 bMonth 02 bday 10
     * from string then converted to integer
     *  //https://www.geeksforgeeks.org/how-to-concatenate-two-integer-values-into-one/
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public Integer concat(int a, int b, int c)
    {
        String s;
        int d;

        // Convert both the integers to string
        String s1 = Integer.toString(a);
        String s2 = Integer.toString(b);
        String s3 = Integer.toString(c);

        if(b < 10){
            s = s1 + "0" + s2 + s3;
            d = Integer.parseInt(s);

        }else{
            s = s1 + s2 + s3;
            d = Integer.parseInt(s);

        }

        return d;
    }

    /**
     * Takes integer bday info and parses it
     *
     * @param f
     * @param birthInfo
     * @throws ParseException
     */
    public void addInformation(Friend f , Integer birthInfo) throws ParseException{

        //using concat and parsing into a particular format, and converting to a
        //date type

        String s = birthInfo.toString();
        Date d1 = sdfOld.parse(birthInfo.toString());
        //friend exact date
        LocalDate friendbDate = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


        //if date has passed
        if(friendbDate.isBefore(currDate) == true){
            Integer bdayNotPast = concat( currDate.getYear() + 1, friend.getbMonth(), friend.getbDay());

            Date d2 = sdfOld.parse(bdayNotPast.toString());
            LocalDate friendbDate2 = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


            long l = d2.getTime() - tDate.getTime();


            //converting to hours
            long timeDiff = TimeUnit.MILLISECONDS.toHours(l);

            //adding countdown info to Friend
            friend.setCountdown(timeDiff/24);
            friend.setHourDiff(timeDiff);

        }
        else{
            //finds the different between bday and current day in milliseconds
            long l = d1.getTime() - tDate.getTime();

            //converting to hours
            long timeDiff = TimeUnit.MILLISECONDS.toHours(l);

            //adding countdown info to Friend
            friend.setCountdown(timeDiff/24);
            friend.setHourDiff(timeDiff);
        }

    }


    /**
     * takes in the friend list and organizes it to most recent birthdays
     * connects to friend adapter that populates the list with the items
     * @param list
     */
    private void setListView(LinkedList<Friend> list) {

        String s;


        //sorts the friends
        Collections.sort(list,new CompareBday());
        for(Friend f: list){
            closestBdaySLL.add(f);

        }

        listView = findViewById(R.id.listView);
        FriendAdapter myAdapter = new FriendAdapter(this,R.layout.closest_bday,closestBdaySLL);

        listView.setAdapter(myAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                friend = (Friend) listView.getItemAtPosition(position);

                Bundle info = new Bundle();
                //putting edited friend in bundle
                info.putSerializable("friend", friend);
                info.putSerializable("userId", userId);

                // do need these
                Intent save = new Intent(ClosestBday.this, FriendPage.class);
                save.putExtras(info);
                startActivity(save);

            }

        });


    }

    @Override
    public void onClick(View view) {


        if(view.getId() == R.id.addFriend){
            Intent intent = new Intent(ClosestBday.this, EditFriendPage.class);

            Bundle info = new Bundle();

            //putting email in bundle
            info.putSerializable("userId", userId);
           // info.putSerializable("friend", friend);
            intent.putExtras(info);
            startActivity(intent);

        }
        else if(view.getId() == R.id.birthdays){
            Intent intent = new Intent(ClosestBday.this, ClosestBday.class);

            Bundle info = new Bundle();

            //putting email in bundle
            info.putSerializable("userId", userId);
            // info.putSerializable("friend", friend);
            intent.putExtras(info);
            startActivity(intent);

        }
        else if(view.getId() == R.id.settings){
            Intent intent = new Intent(ClosestBday.this, Recommendation.class);

            Bundle info = new Bundle();

            //putting email in bundle
            info.putSerializable("userId", userId);
            // info.putSerializable("friend", friend);
            intent.putExtras(info);
            startActivity(intent);

        }

    }


}

