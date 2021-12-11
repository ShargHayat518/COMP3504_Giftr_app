package com.example.giftr_app;

import java.util.Comparator;

public class CompareBday extends Friend implements Comparator<Friend>{


    @Override
    public int compare(Friend one, Friend two){

        int order = Long.compare( two.getCountdown(),one.getCountdown());

        if(one.getCountdown() < two.getCountdown()){
            return -1;
        }
        else{
            return 1;
        }

    }
}