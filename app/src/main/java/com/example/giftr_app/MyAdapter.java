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


public class MyAdapter extends ArrayAdapter<String> {


    Context context;
    int images;
    String[] link;
    String[] price;
    String[] title;

//    ArrayList images;
//    ArrayList link;
//    ArrayList price;
//    ArrayList title;

    public MyAdapter(@NonNull Context context, String [] link, int images, String[] price, String[] title) {
        super(context, R.layout.recommendations, R.id.linkView,link);

        this.context = context;
        this.images = images;
        this.link = link;
        this.price = price;
        this.title = title;

    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        View singleItem = convertView;
        ProgramViewHolder holder;

        if(singleItem == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.recommendations,parent, false);
            holder = new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);
        }
        else{
            holder = (ProgramViewHolder) singleItem.getTag();
        }

        holder.itemImage.setImageResource(images);
        holder.url.setText(link[position]);
        holder.recommend.setText(title[position]);
        holder.cost.setText("$" + price[position]);


        return singleItem;
    }



}
