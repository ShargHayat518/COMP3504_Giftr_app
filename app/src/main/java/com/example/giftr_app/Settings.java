package com.example.giftr_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class displays the settings page
 */
public class Settings extends AppCompatActivity implements View.OnClickListener {

    Button viewEmail;
    Button changePass;
    Button logout;
    Button privacy;
    Button terms;
    String userId;
    Intent next;
    Button settings;
    Button birthdays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.viewEmail).setOnClickListener(this);
        findViewById(R.id.changePass).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.privacy).setOnClickListener(this);
        findViewById(R.id.terms).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.birthdays).setOnClickListener(this);

        viewEmail=findViewById(R.id.viewEmail);
        changePass=findViewById(R.id.changePass);
        logout=findViewById(R.id.logout);
        privacy=findViewById(R.id.privacy);
        terms=findViewById(R.id.terms);
        settings=findViewById(R.id.settings);
        birthdays=findViewById(R.id.birthdays);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        userId = (String) bundle.getSerializable("userId");


    }


    @Override
    public void onClick(View view) {

        // displays user's email if they click
        if (view.getId() == R.id.viewEmail) {
            viewEmail.setText(userId);
        }

        // nav bar button to settings page
        else if(view.getId() == R.id.settings){
            Toast.makeText(this, "You are already on the settings page", Toast.LENGTH_SHORT).show();
        }

        // if they are leaving the page
        else {

            // nav bar button to birthdays page
            if(view.getId() == R.id.birthdays){
                next = new Intent(Settings.this, ClosestBday.class);
            }

            // takes user to change password page
            else if (view.getId() == R.id.changePass) {
                next = new Intent(Settings.this, ChangePass.class);
            }

            // takes user to login page
            else if (view.getId() == R.id.logout) {
                next = new Intent(Settings.this, Login.class);
            }
            // takes the user to the privacy page
            else if (view.getId() == R.id.privacy) {
                next = new Intent(Settings.this, Privacy.class);

            }
            // takes the user to the terms page
            else if (view.getId() == R.id.terms) {
                next = new Intent(Settings.this, Terms.class);

            }

            // put navigation bar items here

            Bundle info = new Bundle();

            //putting username in bundle
            info.putSerializable("userId", userId);
            next.putExtras(info);

            startActivity(next);

        }
    }
}
