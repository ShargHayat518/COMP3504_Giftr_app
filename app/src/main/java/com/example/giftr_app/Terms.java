package com.example.giftr_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Terms of service class. Displays terms of service and holds the navigation bar.
 */
public class Terms extends AppCompatActivity implements View.OnClickListener {
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //userId = (String) bundle.getSerializable("userId");

    }

    @Override
    public void onClick(View view) {


    }
}