package com.example.giftr_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FriendPage extends AppCompatActivity implements View.OnClickListener {

    Friend friend;
    String userId = "hi";
    TextView image;
    Intent next;

    TextView textViewTitle;
    TextView textViewURL;
    TextView textViewPrice;

    TextView textViewTitle2;
    TextView textViewURL2;
    TextView textViewPrice2;
    TextView textViewCount;

    ImageView logo;
    ImageView logo2;

    String[] interestArray;
    ArrayList<String> interestAList = new ArrayList<>();

    ListView listView;
    public String link[];
    public String price[];
//    public int icons[] = {R.drawable.amz, R.drawable.aliexpress, R.drawable.wish};
    public String []title;


    float min;
    float max;

    @Override
    public void onClick(final View v) {
        //takes user to edit friend page
        if(v.getId() == R.id.moreButton){
            next = new Intent(this, Recommendation.class);

        }
        else if(v.getId() == R.id.editFriend){
            next = new Intent(this, EditFriendPage.class);
        }

        // for testing
        Bundle info = new Bundle();

        //putting edited friend in bundle
        info.putSerializable("friend", friend);
        info.putSerializable("userId", userId);

        next.putExtras(info);

        startActivity(next);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendpage);


        textViewTitle = findViewById(R.id.linkView);
        textViewURL = findViewById(R.id.url);
        textViewPrice = findViewById(R.id.price);

        textViewTitle2 = findViewById(R.id.linkView2);
        textViewURL2 = findViewById(R.id.url2);
        textViewPrice2 = findViewById(R.id.price2);

        logo = findViewById(R.id.icon);
        logo2 = findViewById(R.id.icon2);

        findViewById(R.id.editFriend).setOnClickListener(this);
        findViewById(R.id.moreButton).setOnClickListener(this);

        image = findViewById(R.id.friendPic1);

        //receiving friend object
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        textViewCount = findViewById(R.id.countDownText);


        friend = (Friend) bundle.getSerializable("friend");
        userId = (String) bundle.getSerializable("userId");


        //getting friend name
        String name = friend.getName();
        Long getCount = friend.getCountdown();

        //converting long count to string countd
        String count = Long.toString(friend.getCountdown());
        textViewCount.setText(count + "days");


        String s = friend.getStringInterests();

        interestArray = s.split(",");
        //move array into arraylist
        interestAList.addAll(Arrays.asList(interestArray));


        for(String f : interestAList){
            try {
                fetchAmzData(f);


            } catch (IOException e) {
                Toast.makeText(this,"No recommendations yet!",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        //replacing placeholder with friend's name
        TextView friendName = findViewById(R.id.friendName);
        friendName.setText(name);
        image.setBackgroundColor(friend.getColour());

    }

    public void fetchAmzData(String interest) throws IOException {


        listView = findViewById(R.id.listView);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();


        Request request = new Request.Builder()
                .url("https://amazon23.p.rapidapi.com/product-search?query="+interest)
                .get()
                .addHeader("x-rapidapi-host", "amazon23.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "4618320329mshbc0568d0efbf48ap11ff75jsn6ce363a6a28a")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                Toast.makeText(FriendPage.this, "Error +", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resp = response.body().string();

                if (response.isSuccessful()) {

                    //https://stackoverflow.com/questions/24246783/okhttp-response-callbacks-on-the-main-thread
                    //call back runs on background thread, need to put it back on the main thread
                    FriendPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new JSONObject(resp);
                                JSONArray jArray = jsonObject.getJSONArray("result");
                                title = new String[jArray.length()];
                                price = new String[jArray.length()];
                                link = new String[jArray.length()];

                                for (int i = 0; i < jArray.length(); i++) {

                                    JSONObject obj = jArray.getJSONObject(i);

                                    String product_title = obj.getString("title");
                                    String product_price = obj.getString("price");
                                    JSONObject jsonObjectPrice = new JSONObject(product_price);
                                    String actualPrice = jsonObjectPrice.getString("current_price");

                                    String product_url = obj.getString("url");

                                    title[i] = product_title;
                                    price[i] = actualPrice;
                                    link[i] = product_url;

                                }

                                textViewTitle.setText(title[1]);
                                textViewPrice.setText(price[1]);
                                textViewURL.setText(link[1]);
                                logo.setImageResource(R.drawable.amz);

                                textViewTitle2.setText(title[2]);
                                textViewPrice2.setText(price[2]);
                                textViewURL2.setText(link[2]);
                                logo.setImageResource(R.drawable.amz);
                                logo2.setImageResource(R.drawable.amz);

//                                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), link,  R.drawable.amz,price,title);
//                                listView.setAdapter(myAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

            }

        });

    }

    public void fetchAliData(String interest) throws IOException {


        listView = findViewById(R.id.listView);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();


        //"https://magic-aliexpress1.p.rapidapi.com/api/products/search?minSalePrice=5&sort=ORDER_DESC&page=1&maxSalePrice=20&name="+interest

        Request request = new Request.Builder()
                .url("https://magic-aliexpress1.p.rapidapi.com/api/products/search?minSalePrice=" + min + "&sort=ORDER_DESC&page=1&maxSalePrice=" + max + "&name="+interest)
                .get()
                .addHeader("x-rapidapi-host", "magic-aliexpress1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "4618320329mshbc0568d0efbf48ap11ff75jsn6ce363a6a28a")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                Toast.makeText(FriendPage.this, "Error +", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resp = response.body().string();

                if (response.isSuccessful()) {

                    //https://stackoverflow.com/questions/24246783/okhttp-response-callbacks-on-the-main-thread
                    //call back runs on background thread, need to put it back on the main thread
                    FriendPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new JSONObject(resp);
                                JSONArray jArray = jsonObject.getJSONArray("docs");
                                title = new String[jArray.length()];
                                price = new String[jArray.length()];
                                link = new String[jArray.length()];

                                for (int i = 0; i < jArray.length(); i++) {

                                    JSONObject obj = jArray.getJSONObject(i);

                                    String product_title = obj.getString("product_title");
                                    String product_price = obj.getString("app_sale_price");
                                    String product_url = obj.getString("product_detail_url");

                                    //converting to CAD
                                    double d = Double.valueOf(product_price) * 1.26;
                                    String s = Double.toString(d);

                                    title[i] = product_title;
                                    price[i] = s;
                                    link[i] = product_url;

                                }

                                textViewTitle.setText(title[1]);
                                textViewPrice.setText(price[1]);
                                textViewURL.setText(link[1]);

                                textViewTitle2.setText(title[2]);
                                textViewPrice2.setText(price[2]);
                                textViewURL2.setText(link[2]);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

            }

        });

    }

}