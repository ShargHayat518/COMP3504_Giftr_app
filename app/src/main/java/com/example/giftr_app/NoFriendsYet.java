package com.example.giftr_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;

public class NoFriendsYet extends AppCompatActivity implements View.OnClickListener{


    String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nofriends);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.addFriend).setOnClickListener(this);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        userId = (String) bundle.getSerializable("userId");



    }

    @Override
    public void onClick(View view) {


        if(view.getId() == R.id.back){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.addFriend){



            Intent intent = new Intent(NoFriendsYet.this, EditFriendPage.class);

            Bundle info = new Bundle();

            //putting email in bundle
            info.putSerializable("userId", userId);
            intent.putExtras(info);
            startActivity(intent);
        }
    }
}
