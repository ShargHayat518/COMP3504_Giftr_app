package com.example.giftr_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


/**
 * This is a customized adapter class and information was from
 * https://developer.android.com/reference/android/widget/ListView
 *
 * This adapter class
 */

public class FriendAdapter extends ArrayAdapter<Friend> {

    private Context mContext;
    int mResource;
    LinkedList<Friend> friend;

    private LinkedList<Friend> fll;
    private LinkedList<Friend> searchFriends;

    /**
     * Constructor method
     *
     * @param context
     * @param
     * @param friend
     */

    public FriendAdapter(Context context, int resource,  LinkedList<Friend> friend) {
        super(context,0, friend);
        mContext = context;
       mResource = resource;
       this.friend = friend;

    }

    /**
     * overridden method that gets the View and displays it in a listview format
     *
     * this method reuses tool object to provide user with a smoother experience when scrolling.
     *
     *
     * @param position
     * @param convertView
     * @param parent
     * @return View that will be displayed
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        // gets the info about tools at the position its currently at
        String fName = getItem(position).getName();
        int image = getItem(position).getColour();
        Long countdwn = getItem(position).getCountdown();


        //if this View is null create a new view,
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            //layout file you want to inflate
         //   convertView = inflater.inflate(R.layout.closest_bday, parent,false);
            convertView = inflater.inflate(mResource, parent,false);
        }

        //if convert view is already available reuse it and find using specified ID
        TextView imageView = convertView.findViewById(R.id.profilePic);
        TextView friendName = (TextView) convertView.findViewById(R.id.friendName);
        TextView countDown = (TextView) convertView.findViewById(R.id.countDown);


        //populates the textView with the information retrieved from the getters
        imageView.setBackgroundColor(image);
        friendName.setText(fName);
        countDown.setText(countdwn + " days remaining");

        return convertView;
    }

}
